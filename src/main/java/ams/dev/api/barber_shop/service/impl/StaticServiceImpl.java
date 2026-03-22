package ams.dev.api.barber_shop.service.impl;

import ams.dev.api.barber_shop.dto.ApiResponseDto;
import ams.dev.api.barber_shop.dto.pagination.PageResponseDto;
import ams.dev.api.barber_shop.dto.service.ServiceFilterDto;
import ams.dev.api.barber_shop.dto.service.ServiceRequestDto;
import ams.dev.api.barber_shop.dto.service.ServiceResponseDto;
import ams.dev.api.barber_shop.entity.BarberShopEntity;
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
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.*;
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
                service -> service.getIsActive() ? "Activo" : "Inactivo"
        );

        ExcelReportConfig<ServiceEntity> config = ExcelReportConfig.<ServiceEntity>builder()
                .sheetName("Servicios")
                .headers(new String[]{"ID","Nombre del Servicio","Duración","Precio","Estado"})
                .fieldExtractors(extractorStaticServiceList)
                .title("Reporte de Servicios")
                .build();

        return generateExcel.generateExcel(serviceListBD, config);
    }

    @Override
    public ApiResponseDto executeImportMassiveService(MultipartFile file, String indicator) {
        try {
            LOGGER.info("FILE {}", file.getName());
            LOGGER.info("INDICADOR {}", indicator);
            List<ServiceRequestDto> serviceList = this.dataStaticServiceExcel(file.getInputStream(), indicator);
            int createdCount=0;
            int updatedCount=0;

            LOGGER.info("LISTADO DE REGISTROS EXTRAIDOS DEL EXCEL {}", serviceList.size());
            for (ServiceRequestDto serviceRequestDto: serviceList){
                ServiceEntity serviceEntity;
                ServiceEntity serviceBD;
                BarberShopEntity barberShopBD;

                if (serviceRequestDto.getServiceId() == null || serviceRequestDto.getServiceId().trim().isEmpty()) {
                    LOGGER.info("SE CREAR SERVICIO");
                    serviceEntity = new ServiceEntity();
                    serviceEntity.setIsActive(true);
                    createdCount++;
                } else{
                    LOGGER.info("SE ACTUALIZA SERVICIO CON ID {}", serviceRequestDto.getServiceId());
                    serviceBD = this.findById(serviceRequestDto.getServiceId(),"31ec2564-0c37-403a-be08-38b31346d508");
                    serviceEntity = serviceBD;
                    serviceEntity.setIsActive(serviceRequestDto.getIsActive());
                    updatedCount++;
                }

                barberShopBD = this.barberShopService.findById("31ec2564-0c37-403a-be08-38b31346d508");
                serviceEntity.setName(serviceRequestDto.getName());
                serviceEntity.setDuration(serviceRequestDto.getDuration());
                serviceEntity.setPrice(serviceRequestDto.getPrice());
                serviceEntity.setIsDeleted(false);
                serviceEntity.setBarbershop(barberShopBD);

                serviceRepository.save(serviceEntity);
            }
            LOGGER.info("SERVICIOS CREADOS {}", createdCount);
            LOGGER.info("SERVICIOS ACTUALIZADOS {}", updatedCount);

            return new ApiResponseDto("Servicios registrado exitosamente");

        }catch (Exception e){
            LOGGER.error("ERROR AL PROCESAR EL ARCHIVO {}", e);
            throw new BusinessException("Error al procesar el archivo excel");
        }
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

    private List<ServiceRequestDto> dataStaticServiceExcel(InputStream inputStream, String indicator){
        try(Workbook workbook = new XSSFWorkbook(inputStream)) {
            List<ServiceRequestDto> staticServiceList = new ArrayList<>();
            Sheet sheet = typeSheet(workbook,indicator);
            if (sheet == null)
                throw new BusinessException("No se encontro la hoja en el archivo excel");

            if (sheet.getLastRowNum() < 1)
                throw new BusinessException("No hay registros en el archivo excel");

            for (int i = 1; i <= sheet.getLastRowNum(); i++){
                Row row = sheet.getRow(i);
                if (row==null) continue;

                ServiceRequestDto serviceRequestDto = this.formatedServiceDto(row,indicator);
                staticServiceList.add(serviceRequestDto);
            }
            LOGGER.info("TOTAL DE REGISTRSO EN EXCEL RECUPERADOS {} ", staticServiceList.size());
            return staticServiceList;
        }catch (Exception e){
            LOGGER.error("EEROR AL PROCESAR LA INFORMACIÓN DEL EXCEL", e);
            throw new BusinessException("Error el importar los servicios");
        }
    }

    private ServiceRequestDto formatedServiceDto(Row row, String indicator){
        ServiceRequestDto serviceRequestDto;

        // SI EL INDICADOR ES UPDATED MASSIVE
        if (Constants.INDICATOR_UPDATED_MASSIVE.equals(indicator)){
            LOGGER.info("SETEA DATOS PARA ACTULIZAR SERVICIOS");
            serviceRequestDto = new ServiceRequestDto();
            serviceRequestDto.setServiceId(getCellValue(row.getCell(0)));
            serviceRequestDto.setName(getCellValue(row.getCell(1)));
            LocalTime duration = getLocalTimeFromCell(row.getCell(2));
            serviceRequestDto.setDuration(duration);
            serviceRequestDto.setPrice(new BigDecimal(getCellValue(row.getCell(3))));
            serviceRequestDto.setIsActive(!Constants.STATUS_INACTIVE.equals(getCellValue(row.getCell(4))));
        }else{
            LOGGER.info("SETEA PARA CREAR SERVICIOS");
            serviceRequestDto = new ServiceRequestDto();
            serviceRequestDto.setName(getCellValue(row.getCell(0)));
            LocalTime duration = getLocalTimeFromCell(row.getCell(1));
            serviceRequestDto.setDuration(duration);
            serviceRequestDto.setPrice(new BigDecimal(getCellValue(row.getCell(2))));
        }

        return serviceRequestDto;
    }

    private Sheet typeSheet(Workbook workbook, String indicator){
        Sheet sheet;
        if (Constants.INDICATOR_IMPORT_MASSIVE.equals(indicator))
            sheet = workbook.getSheetAt(0);
        else if(Constants.INDICATOR_UPDATED_MASSIVE.equals(indicator))
            sheet = workbook.getSheetAt(1);
        else
            throw new BusinessException("PARAMETROS INVALIDOS");
        return sheet;
    }

    private String getCellValue(Cell cell){
        if (cell == null) return "";

        return switch (cell.getCellType()){
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }

    private LocalTime getLocalTimeFromCell(Cell cell) {
        if (cell == null) return null;

        try {
            switch (cell.getCellType()) {
                case NUMERIC:
                    // Usar DateUtil de Apache POI para verificar si es fecha/hora
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return cell.getLocalDateTimeCellValue().toLocalTime();
                    } else {
                        // Convertir número decimal a tiempo
                        double excelTime = cell.getNumericCellValue();
                        return convertExcelTimeToLocalTime(excelTime);
                    }

                case STRING:
                    String timeStr = cell.getStringCellValue().trim();
                    if (!timeStr.isEmpty())
                        return LocalTime.parse(timeStr); // Intentar parsear formato HH:mm:ss
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            LOGGER.error("Error al convertir celda a LocalTime: {}", cell, e);
        }
        return null;
    }

    private LocalTime convertExcelTimeToLocalTime(double excelTime) {
        // Fórmula: totalSegundos = excelTime * 24 * 3600
        long totalSeconds = Math.round(excelTime * 24 * 3600);

        // Asegurar que está dentro del rango válido
        if (totalSeconds < 0) totalSeconds = 0;
        if (totalSeconds >= 86400) totalSeconds = 86399;

        int hours = (int) (totalSeconds / 3600);
        int minutes = (int) ((totalSeconds % 3600) / 60);
        int seconds = (int) (totalSeconds % 60);

        return LocalTime.of(hours, minutes, seconds);
    }



}
