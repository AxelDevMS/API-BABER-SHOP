package ams.dev.api.barber_shop.service;

import ams.dev.api.barber_shop.dto.UserRequestDto;
import ams.dev.api.barber_shop.entity.UserEntity;

public interface UserService {

    UserEntity executeCreateUser(UserRequestDto userRequestDto);
}
