package ams.dev.api.barber_shop.mapper.request;

import ams.dev.api.barber_shop.dto.BarberShopRequestDto;
import ams.dev.api.barber_shop.entity.BarberShopEntity;
import ams.dev.api.barber_shop.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BarberShopRequestMapper {


    public BarberShopEntity toConvertEntity(BarberShopRequestDto barberShopRequestDto){
        if (barberShopRequestDto == null) return null;

        BarberShopEntity barberShopEntity = new BarberShopEntity();
        barberShopEntity.setName(barberShopRequestDto.getName());
        barberShopEntity.setCommercialName(barberShopRequestDto.getCommercialName());
        barberShopEntity.setTaxId(barberShopRequestDto.getTaxId());
        barberShopEntity.setAddress(barberShopRequestDto.getAddress());
        barberShopEntity.setPhone(barberShopRequestDto.getPhone());
        barberShopEntity.setMobile(barberShopRequestDto.getMobile());
        barberShopEntity.setEmail(barberShopRequestDto.getEmail());
        barberShopEntity.setLogoUrl(barberShopRequestDto.getLogoUrl());
        barberShopEntity.setOpeningTime(barberShopRequestDto.getOpeningTime());
        barberShopEntity.setClosingTime(barberShopRequestDto.getClosingTime());
        return barberShopEntity;
    }

    public BarberShopEntity toConvertEntity(BarberShopRequestDto barberShopRequestDto, UserEntity userEntity){
        if (barberShopRequestDto == null) return null;

        BarberShopEntity barberShopEntity = new BarberShopEntity();
        List<UserEntity> users = new ArrayList<>();
        users.add(userEntity);

        barberShopEntity.setName(barberShopRequestDto.getName());
        barberShopEntity.setCommercialName(barberShopRequestDto.getCommercialName());
        barberShopEntity.setTaxId(barberShopRequestDto.getTaxId());
        barberShopEntity.setAddress(barberShopRequestDto.getAddress());
        barberShopEntity.setPhone(barberShopRequestDto.getPhone());
        barberShopEntity.setMobile(barberShopRequestDto.getMobile());
        barberShopEntity.setEmail(barberShopRequestDto.getEmail());
        barberShopEntity.setLogoUrl(barberShopRequestDto.getLogoUrl());
        barberShopEntity.setOpeningTime(barberShopRequestDto.getOpeningTime());
        barberShopEntity.setClosingTime(barberShopRequestDto.getClosingTime());
        barberShopEntity.setUsers(users);
        return barberShopEntity;
    }
}
