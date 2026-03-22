package ams.dev.api.barber_shop.mapper.request;

import ams.dev.api.barber_shop.dto.service.ServiceRequestDto;
import ams.dev.api.barber_shop.entity.BarberShopEntity;
import ams.dev.api.barber_shop.entity.ServiceEntity;

public class ServiceRequestMapper {

    public static ServiceEntity toEntity(ServiceRequestDto serviceDto) {

        if (serviceDto == null) return null;

        return ServiceEntity
                .builder()
                .name(serviceDto.getName())
                .duration(serviceDto.getDuration())
                .price(serviceDto.getPrice())
                .build()
                ;
    }

    public static ServiceEntity toEntity(ServiceRequestDto serviceDto, BarberShopEntity barberShopEntity) {

        if (serviceDto == null) return null;

        return ServiceEntity
                .builder()
                .name(serviceDto.getName())
                .duration(serviceDto.getDuration())
                .price(serviceDto.getPrice())
                .barbershop(barberShopEntity)
                .build()
                ;
    }



}
