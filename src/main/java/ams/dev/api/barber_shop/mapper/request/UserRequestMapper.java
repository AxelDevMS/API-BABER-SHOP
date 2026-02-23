package ams.dev.api.barber_shop.mapper.request;

import ams.dev.api.barber_shop.dto.UserRequestDto;
import ams.dev.api.barber_shop.entity.RoleEntity;
import ams.dev.api.barber_shop.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserRequestMapper {

    public UserEntity toEntity(UserRequestDto userRequestDto){

        if (userRequestDto == null) return null;

        UserEntity userEntity = new UserEntity();

        userEntity.setFullName(userRequestDto.getFullName());
        userEntity.setPhone(userRequestDto.getPhone());
        userEntity.setUsername(userRequestDto.getUsername());
        userEntity.setPassword(userRequestDto.getPassword());
        userEntity.setEmail(userRequestDto.getEmail());
        return userEntity;
    }


    public UserEntity toEntity(UserRequestDto userRequestDto, RoleEntity role) {

        if (userRequestDto == null) return null;

        UserEntity entity = new UserEntity();
        entity.setFullName(userRequestDto.getFullName());
        entity.setPhone(userRequestDto.getPhone());
        entity.setUsername(userRequestDto.getUsername());
        entity.setPassword(userRequestDto.getPassword());
        entity.setEmail(userRequestDto.getEmail());
        entity.setRole(role);

        return entity;
    }


}
