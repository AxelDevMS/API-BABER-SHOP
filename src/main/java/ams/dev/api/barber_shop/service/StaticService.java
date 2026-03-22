package ams.dev.api.barber_shop.service;

import ams.dev.api.barber_shop.dto.ApiResponseDto;
import ams.dev.api.barber_shop.dto.pagination.PageResponseDto;
import ams.dev.api.barber_shop.dto.service.ServiceFilterDto;
import ams.dev.api.barber_shop.dto.service.ServiceRequestDto;
import ams.dev.api.barber_shop.dto.service.ServiceResponseDto;
import ams.dev.api.barber_shop.entity.ServiceEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StaticService {

    ApiResponseDto executeCreateService(ServiceRequestDto serviceDto);

    ApiResponseDto executeUpdateService(String serviceId, ServiceRequestDto serviceDto);

    ServiceEntity findById(String serviceId, String barbershopId);

    PageResponseDto<ServiceResponseDto> executeListService(ServiceFilterDto queryParamsDto);

    ServiceResponseDto executeGetService(String serviceId, String barbershopId);

    void executeDeleteService(String serviceId, String barbershopId);

    byte[] executeExportGeneralReportService(String serviceId) throws IOException;

    ApiResponseDto executeImportMassiveService(MultipartFile file, String  indicator);

}
