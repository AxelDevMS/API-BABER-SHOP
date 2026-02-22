package ams.dev.api.barber_shop.service.impl;

import ams.dev.api.barber_shop.dto.ApiResponseDto;
import ams.dev.api.barber_shop.dto.BarberShopRequestDto;
import ams.dev.api.barber_shop.entity.BarberShopEntity;
import ams.dev.api.barber_shop.entity.UserEntity;
import ams.dev.api.barber_shop.enums.BarberShopStatus;
import ams.dev.api.barber_shop.exceptions.BusinessException;
import ams.dev.api.barber_shop.mapper.request.BarberShopRequestMapper;
import ams.dev.api.barber_shop.repository.BarberShopRepository;
import ams.dev.api.barber_shop.service.BarberShopService;
import ams.dev.api.barber_shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BarberShopServiceImpl implements BarberShopService {

    @Autowired
    private BarberShopRepository barberShopRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BarberShopRequestMapper barberShopRequestMapper;


    @Override
    public ApiResponseDto executeCreateBarberShop(BarberShopRequestDto barberShopRequestDto) {
        UserEntity userEntity = this.userService.executeCreateUser(barberShopRequestDto.getUser());
        if (userEntity == null)
            throw new BusinessException("No se pudo crear el usuario para la barbería");

        BarberShopEntity barberShopEntity =  this.barberShopRequestMapper.toConvertEntity(barberShopRequestDto,userEntity);
        barberShopEntity.setStatus(BarberShopStatus.ACTIVE);

        barberShopEntity = this.barberShopRepository.save(barberShopEntity);
        return new ApiResponseDto("Barbería registrada con éxito con id: " + barberShopEntity.getId());
    }
}
