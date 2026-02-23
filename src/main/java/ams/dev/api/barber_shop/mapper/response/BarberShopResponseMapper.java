package ams.dev.api.barber_shop.mapper.response;

import ams.dev.api.barber_shop.dto.BarberShopResponseDto;
import ams.dev.api.barber_shop.dto.UserResponseDto;
import ams.dev.api.barber_shop.entity.BarberShopEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BarberShopResponseMapper {

    public BarberShopResponseDto toDto(BarberShopEntity barberShopEntity){
        if (barberShopEntity == null) return null;

        return BarberShopResponseDto
                .builder()
                .id(barberShopEntity.getId())
                .name(barberShopEntity.getName())
                .commercialName(barberShopEntity.getCommercialName())
                .taxId(barberShopEntity.getTaxId())
                .address(barberShopEntity.getAddress())
                .phone(barberShopEntity.getPhone())
                .email(barberShopEntity.getEmail())
                .openingTime(barberShopEntity.getOpeningTime())
                .closingTime(barberShopEntity.getClosingTime())
                .status(barberShopEntity.getStatus())
                .build()
                ;
    }

    public BarberShopResponseDto toDto(BarberShopEntity barberShopEntity, List<UserResponseDto> userList){
        if (barberShopEntity == null) return null;

        return BarberShopResponseDto
                .builder()
                .id(barberShopEntity.getId())
                .name(barberShopEntity.getName())
                .commercialName(barberShopEntity.getCommercialName())
                .taxId(barberShopEntity.getTaxId())
                .address(barberShopEntity.getAddress())
                .phone(barberShopEntity.getPhone())
                .email(barberShopEntity.getEmail())
                .openingTime(barberShopEntity.getOpeningTime())
                .closingTime(barberShopEntity.getClosingTime())
                .status(barberShopEntity.getStatus())
                .users(userList)
                .build()
                ;
    }



}
