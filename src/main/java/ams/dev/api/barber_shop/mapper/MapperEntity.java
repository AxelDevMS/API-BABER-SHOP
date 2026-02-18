package ams.dev.api.barber_shop.mapper;

import ams.dev.api.barber_shop.dto.user.UserRequestDto;
import ams.dev.api.barber_shop.entity.RoleEntity;
import ams.dev.api.barber_shop.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class MapperEntity {

    public UserEntity toUser(UserRequestDto userRequestDto) {
        UserEntity userEntity = new UserEntity();
        RoleEntity roleEntity = new RoleEntity();
        userEntity.setUsername(userRequestDto.getUsername());
        userEntity.setPassword(userRequestDto.getPassword());
        userEntity.setIsActive(userRequestDto.getIsActive());

        roleEntity.setId(userRequestDto.getRole());
        userEntity.setRole(roleEntity);
        return userEntity;
    }

}
