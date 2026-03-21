package ams.dev.api.barber_shop.controller;

import ams.dev.api.barber_shop.dto.ApiResponseDto;
import ams.dev.api.barber_shop.dto.pagination.PageParamRequestDto;
import ams.dev.api.barber_shop.dto.pagination.PageResponseDto;
import ams.dev.api.barber_shop.dto.service.ServiceFilterDto;
import ams.dev.api.barber_shop.dto.service.ServiceRequestDto;
import ams.dev.api.barber_shop.dto.service.ServiceResponseDto;
import ams.dev.api.barber_shop.service.StaticService;
import ams.dev.api.barber_shop.util.GenerateExcelUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/service")
public class StaticServiceController {

    @Autowired
    private StaticService staticService;

    @Autowired
    private GenerateExcelUtil generateExcelUtil;


    @PostMapping
    public ResponseEntity<ApiResponseDto> executeCreateStaticService(@Valid @RequestBody ServiceRequestDto serviceDto) {
        ApiResponseDto responseDto = this.staticService.executeCreateService(serviceDto);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{serviceId}")
    public ResponseEntity<ApiResponseDto> executeUpdateStaticService(
            @PathVariable String serviceId,
            @Valid @RequestBody ServiceRequestDto serviceDto
    ){
        return ResponseEntity.ok(this.staticService.executeUpdateService(serviceId, serviceDto));
    }

    @GetMapping("/{serviceId}/{barbershopId}")
    public ResponseEntity<ServiceResponseDto> executeGetStaticService(@PathVariable String serviceId, @PathVariable String barbershopId) {
        return ResponseEntity.ok(this.staticService.executeGetService(serviceId,barbershopId));
    }

    @GetMapping
    public ResponseEntity<PageResponseDto<ServiceResponseDto>> executeGetListStaticService(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(required = true) String barbershopId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAfter,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdBefore,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String serviceId,
            @RequestParam(required = false) Boolean deleted,
            @RequestParam(required = false) Boolean active
    )
    {
        PageParamRequestDto pageParamDto = new PageParamRequestDto(page, size, sortBy, sortDirection);
        ServiceFilterDto queryParamDto = new ServiceFilterDto();
        queryParamDto.setBarbershopId(barbershopId);
        queryParamDto.setSearchTerm(search);
        queryParamDto.setServiceId(serviceId);
        queryParamDto.setIsDeleted(deleted);
        queryParamDto.setIsActive(active);
        queryParamDto.setCreatedAfter(createdAfter);
        queryParamDto.setCreatedBefore(createdBefore);
        queryParamDto.setPageParam(pageParamDto);

        return ResponseEntity.ok(this.staticService.executeListService(queryParamDto));
    }

    @DeleteMapping("/{serviceId}/{barbershopId}")
    public ResponseEntity<Void> executeDeleteStaticService(@PathVariable String serviceId, @PathVariable String barbershopId){
        this.staticService.executeDeleteService(serviceId,barbershopId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/report")
    public ResponseEntity<byte[]> executeGenerateReportClient(@RequestParam(required = false) String barbershopId) {
        try {
            byte[] excelContent = this.staticService.executeExportGeneralReportService(barbershopId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", generateExcelUtil.generateFileName("reporte_clientes"));

            return new ResponseEntity<>(excelContent, headers, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
