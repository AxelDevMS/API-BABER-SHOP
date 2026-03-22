package ams.dev.api.barber_shop.mapper.response;


import ams.dev.api.barber_shop.dto.BarberShopResponseDto;
import ams.dev.api.barber_shop.dto.service.ServiceResponseDto;
import ams.dev.api.barber_shop.entity.ServiceEntity;

public class ServiceResponseMapper {

    public static ServiceResponseDto toDto(ServiceEntity serviceEntity) {

        if (serviceEntity == null) return null;

        return ServiceResponseDto
                .builder()
                .serviceId(serviceEntity.getId())
                .name(serviceEntity.getName())
                .duration(serviceEntity.getDuration())
                .price(serviceEntity.getPrice())
                .isActive(serviceEntity.getIsActive())
                .isDeleted(serviceEntity.getIsDeleted())
                .createdAt(serviceEntity.getCreatedAt())
                .build();
    }

    public static ServiceResponseDto toDto(ServiceEntity serviceEntity, BarberShopResponseDto barberShopDto) {

        if (serviceEntity == null) return null;

        return ServiceResponseDto
                .builder()
                .serviceId(serviceEntity.getId())
                .name(serviceEntity.getName())
                .duration(serviceEntity.getDuration())
                .price(serviceEntity.getPrice())
                .isActive(serviceEntity.getIsActive())
                .isDeleted(serviceEntity.getIsDeleted())
                .createdAt(serviceEntity.getCreatedAt())
                .barbershop(barberShopDto)
                .build();
    }


}
