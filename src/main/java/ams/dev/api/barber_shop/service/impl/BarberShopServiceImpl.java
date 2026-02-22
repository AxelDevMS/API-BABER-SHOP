package ams.dev.api.barber_shop.service.impl;

import ams.dev.api.barber_shop.dto.ApiResponseDto;
import ams.dev.api.barber_shop.dto.BarberShopRequestDto;
import ams.dev.api.barber_shop.dto.BarberShopResponseDto;
import ams.dev.api.barber_shop.dto.UserResponseDto;
import ams.dev.api.barber_shop.entity.BarberShopEntity;
import ams.dev.api.barber_shop.entity.UserEntity;
import ams.dev.api.barber_shop.enums.BarberShopStatus;
import ams.dev.api.barber_shop.exceptions.BusinessException;
import ams.dev.api.barber_shop.exceptions.ResourceNotFoundException;
import ams.dev.api.barber_shop.mapper.request.BarberShopRequestMapper;
import ams.dev.api.barber_shop.mapper.response.BarberShopResponseMapper;
import ams.dev.api.barber_shop.repository.BarberShopRepository;
import ams.dev.api.barber_shop.service.BarberShopService;
import ams.dev.api.barber_shop.service.EmailService;
import ams.dev.api.barber_shop.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BarberShopServiceImpl implements BarberShopService {

    @Autowired
    private BarberShopRepository barberShopRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BarberShopRequestMapper barberShopRequestMapper;

    @Autowired
    private BarberShopResponseMapper barberShopResponseMapper;

    @Autowired
    private EmailService emailService;


    @Transactional
    @Override
    public ApiResponseDto executeCreateBarberShop(BarberShopRequestDto barberShopRequestDto) {
        UserEntity userEntity = this.userService.executeCreateUser(barberShopRequestDto.getUser());
        if (userEntity == null)
            throw new BusinessException("No se pudo crear el usuario para la barbería");

        BarberShopEntity barberShopEntity =  this.barberShopRequestMapper.toConvertEntity(barberShopRequestDto,userEntity);
        barberShopEntity.setStatus(BarberShopStatus.ACTIVE);

        barberShopEntity = this.barberShopRepository.save(barberShopEntity);

        emailService.sendEmailCredentialsAcces(
                userEntity.getEmail(),
                userEntity.getName()+" "+userEntity.getLastName(),
                userEntity.getEmail(),
                barberShopRequestDto.getUser().getPassword()
        );
        return new ApiResponseDto("Barbería registrada con éxito con id: " + barberShopEntity.getId());
    }

    @Override
    public List<BarberShopResponseDto>  executeGetListBarberShop() {
        List<BarberShopEntity> barberShopListBD = this.barberShopRepository.findAll();

        if (barberShopListBD.isEmpty())
            throw new ResourceNotFoundException("No hay registros en el sistema");

        return barberShopListBD.stream().map(barber->{
            List<UserResponseDto> userListBD = this.userService.executeUsersByBarberShopId(barber.getId());

            return this.barberShopResponseMapper.toDto(barber, userListBD);
        }).toList();
    }

    @Override
    public BarberShopResponseDto executeGetBarberShop(String barberShopId) {
        BarberShopEntity barberShopBD = this.barberShopRepository.findById(barberShopId)
                .orElseThrow(()->new ResourceNotFoundException("BarberShop","id", barberShopId));

        List<UserResponseDto> userListBD = this.userService.executeUsersByBarberShopId(barberShopId);

        return this.barberShopResponseMapper.toDto(barberShopBD,userListBD);
    }

    @Override
    public ApiResponseDto executeUpdateBarberShop(String barberShopId, BarberShopRequestDto barberShopRequestDto) {
        BarberShopEntity barberBD = this.findById(barberShopId);

        barberBD.setName(barberShopRequestDto.getName());
        barberBD.setCommercialName(barberShopRequestDto.getCommercialName());
        barberBD.setTaxId(barberShopRequestDto.getTaxId());
        barberBD.setAddress(barberShopRequestDto.getAddress());
        barberBD.setPhone(barberShopRequestDto.getPhone());
        barberBD.setMobile(barberShopRequestDto.getMobile());
        barberBD.setEmail(barberShopRequestDto.getEmail());
        barberBD.setLogoUrl(barberShopRequestDto.getLogoUrl());
        barberBD.setOpeningTime(barberShopRequestDto.getOpeningTime());
        barberBD.setClosingTime(barberShopRequestDto.getClosingTime());
        barberBD.setTimezone(barberShopRequestDto.getTimezone());

        barberBD = barberShopRepository.save(barberBD);
        return new ApiResponseDto("Los Datos de la barbería se actualizado con éxito con id: " + barberBD.getId());
    }

    @Override
    public void executeDeletedBarberShop(String barberShopId) {
        BarberShopEntity barberBD = this.findById(barberShopId);

        barberBD.setStatus(BarberShopStatus.DELETED);
        barberShopRepository.save(barberBD);
    }

    private BarberShopEntity findById(String barberShopId){
        return barberShopRepository.findById(barberShopId).orElseThrow(()->new ResourceNotFoundException("BarberShop","ID",barberShopId));
    }
}
