package ams.dev.api.barber_shop.service.impl;

import ams.dev.api.barber_shop.dto.ApiResponseDto;
import ams.dev.api.barber_shop.dto.pagination.PageResponseDto;
import ams.dev.api.barber_shop.dto.service.ServiceFilterDto;
import ams.dev.api.barber_shop.dto.service.ServiceRequestDto;
import ams.dev.api.barber_shop.dto.service.ServiceResponseDto;
import ams.dev.api.barber_shop.entity.BarberShopEntity;
import ams.dev.api.barber_shop.entity.ClientEntity;
import ams.dev.api.barber_shop.entity.ServiceEntity;
import ams.dev.api.barber_shop.enums.Constants;
import ams.dev.api.barber_shop.exceptions.BusinessException;
import ams.dev.api.barber_shop.exceptions.ResourceNotFoundException;
import ams.dev.api.barber_shop.mapper.request.ServiceRequestMapper;
import ams.dev.api.barber_shop.mapper.response.ServiceResponseMapper;
import ams.dev.api.barber_shop.repository.ServiceRepository;
import ams.dev.api.barber_shop.repository.specification.ServiceSpecification;
import ams.dev.api.barber_shop.service.BarberShopService;
import ams.dev.api.barber_shop.service.StaticService;
import ams.dev.api.barber_shop.util.ExcelReportConfig;
import ams.dev.api.barber_shop.util.GenerateExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class StaticServiceImpl implements StaticService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private BarberShopService barberShopService;

    @Autowired
    private GenerateExcelUtil generateExcel;

    private static final Logger LOGGER = LoggerFactory.getLogger(StaticServiceImpl.class);

    @Override
    public ApiResponseDto executeCreateService(ServiceRequestDto serviceDto) {
        LOGGER.info("=== INGRESANDO METODO PARA CREAR SERVICIO DE ESTETICA ==");
        LOGGER.info("IDENTIFICADOR BARBERSHOP_ID {}",serviceDto.getBarbershop().getId());

        BarberShopEntity barberShopBD = this.barberShopService.findById(serviceDto.getBarbershop().getId());

        ServiceEntity saveService = ServiceRequestMapper.toEntity(serviceDto,barberShopBD);
        saveService.setIsActive(true);
        saveService.setIsDeleted(false);
        saveService = this.serviceRepository.save(saveService);

        LOGGER.info("SERVICIO REGISTRADO CON ID {}", saveService.getId());

        return new ApiResponseDto("Servicio registrado con éxito con id: "+saveService.getId());
    }

    @Override
    public ApiResponseDto executeUpdateService(String serviceId, ServiceRequestDto serviceDto) {
        LOGGER.info("=== INGRESANDO METODO PARA ACTUALIZAR SERVICIO DE ESTETICA ===");
        LOGGER.info("SERVICE ID {}",serviceId);

        ServiceEntity serviceBD = this.findById(serviceId,serviceDto.getBarbershop().getId());

        serviceBD.setName(serviceDto.getName());
        serviceBD.setDuration(serviceDto.getDuration());
        serviceBD.setPrice(serviceDto.getPrice());
        serviceBD.setIsActive(serviceDto.getIsActive());

        this.serviceRepository.save(serviceBD);

        LOGGER.info("SERVICIO ACTUALIZADO CON HORA {}", serviceBD.getUpdatedAt());

        return new ApiResponseDto("Servicio actualizado con éxito con id: "+serviceBD.getId());
    }

    @Override
    public ServiceEntity findById(String serviceId,String barbershopId ) {
        return this.serviceRepository.findByIdAndBarbershopId(serviceId,barbershopId)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Servicio","Id",serviceId)
                );

    }

    @Override
    public PageResponseDto<ServiceResponseDto> executeListService(ServiceFilterDto queryParamsDto) {
        LOGGER.info("=== INGRESANDO METODO PARA FILTRAR SERVICIOS ===");
        LOGGER.info("BARBERSHOP ID: {}", queryParamsDto.getBarbershopId());

        Sort sort = queryParamsDto.getPageParam().getSortDirection().equalsIgnoreCase(Constants.PARAM_DESC) ?
                Sort.by(queryParamsDto.getPageParam().getSortBy()).descending() :
                Sort.by(queryParamsDto.getPageParam().getSortBy()).ascending();

        Pageable pageable = PageRequest.of(queryParamsDto.getPageParam().getPage(),
                queryParamsDto.getPageParam().getSize(), sort);

        Specification<ServiceEntity> spec = ServiceSpecification.combineFromFilter(queryParamsDto);

        Page<ServiceEntity>  serviceListBD = this.serviceRepository.findAll(spec, pageable);
        if (serviceListBD.isEmpty())
            throw new ResourceNotFoundException("No se encontraron servicios en el sistema");

        Page<ServiceResponseDto> serviceList = serviceListBD.map(ServiceResponseMapper::toDto);

        PageResponseDto<ServiceResponseDto> response = new PageResponseDto<>(serviceList);
        LOGGER.info("TOTAL DE REGISTROS ENCONTRADOS: {}", response.getContent().size());

        return response;
    }

    @Override
    public ServiceResponseDto executeGetService(String serviceId, String barbershopId) {
        LOGGER.info("=== INGRESANDO METODO VER DETALLE DEL SERIVICO DE ESTETICA ===");
        LOGGER.info("ID DEL SERVICIO A BUSCAR {}", serviceId);

        this.validateId(serviceId);
        ServiceEntity serviceBD = this.findById(serviceId,barbershopId);

        LOGGER.info("SERVICIO ENCONTRADO {}", serviceBD.getName());
        return ServiceResponseMapper.toDto(serviceBD);
    }

    @Override
    public void executeDeleteService(String serviceId, String barbershopId) {
        LOGGER.info("=== INGRESANDO METODO PARA ELIMINAR SERIVICO DE ESTETICA ===");
        ServiceEntity serviceBD = this.findById(serviceId,barbershopId);

        serviceBD.setIsActive(false);
        serviceBD.setIsDeleted(true);
        serviceBD = this.serviceRepository.save(serviceBD);

        LOGGER.info("SERIVICO DE ESTETICA ELIMINADO {}",serviceBD.getIsDeleted());

    }

    @Override
    public byte[] executeExportGeneralReportService(String serviceId) throws IOException {
        List<ServiceEntity> serviceListBD =this.serviceRepository.findAllByBarbershopId(serviceId);

        List<Function<ServiceEntity,Object>> extractorStaticServiceList = Arrays.asList(
                ServiceEntity::getId,
                ServiceEntity::getName,
                ServiceEntity::getDuration,
                ServiceEntity::getPrice,
                client -> client.getIsActive() ? "Activo" : "Inactivo"
        );

        ExcelReportConfig<ServiceEntity> config = ExcelReportConfig.<ClientEntity>builder()
                .sheetName("Clientes")
                .headers(new String[]{"ID","Nombre","Teléfono","Email","VIP","Estado"})
                .fieldExtractors(extractorStaticServiceList)
                .title("Reporte de Clientes")
                .build();

        return generateExcel.generateExcel(serviceListBD, config);
    }

    /*
    ==============================================================================
                        MÉTODOS PRIVADOS USADOS PARA ESTA CLASE
    ==============================================================================
    */

    private void validateId(String id){
        if (id == null || id.trim().isEmpty()){
            Map<String, List<String>> errors = new HashMap<>();
            errors.put("id",List.of("El ID no puede estar vacío"));
            throw new BusinessException("Error de validación", errors);
        }
    }


}
