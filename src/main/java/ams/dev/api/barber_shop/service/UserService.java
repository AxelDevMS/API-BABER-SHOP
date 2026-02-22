package ams.dev.api.barber_shop.service;

import ams.dev.api.barber_shop.dto.UserRequestDto;
import ams.dev.api.barber_shop.dto.UserResponseDto;
import ams.dev.api.barber_shop.entity.UserEntity;

import java.util.List;

public interface UserService {

    UserEntity executeCreateUser(UserRequestDto userRequestDto);

    List<UserResponseDto> executeUsersByBarberShopId(String barberShopId);
}
