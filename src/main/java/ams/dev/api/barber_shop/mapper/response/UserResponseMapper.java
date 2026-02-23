package ams.dev.api.barber_shop.mapper.response;

import ams.dev.api.barber_shop.dto.RoleResponseDto;
import ams.dev.api.barber_shop.dto.UserResponseDto;
import ams.dev.api.barber_shop.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserResponseMapper {

    public UserResponseDto toDto(UserEntity userEntity){
        if (userEntity == null) return null;

        return UserResponseDto
                .builder()
                .id(userEntity.getId())
                .fullName(userEntity.getFullName())
                .phone(userEntity.getPhone())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .isActive(userEntity.getIsActive())
                .isDeleted(userEntity.getIsDeleted())
                .build()
                ;
    }

    public UserResponseDto toDto(UserEntity userEntity, RoleResponseDto roleDto){
        if (userEntity == null) return null;

        return UserResponseDto
                .builder()
                .id(userEntity.getId())
                .fullName(userEntity.getFullName())
                .phone(userEntity.getPhone())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .isActive(userEntity.getIsActive())
                .isDeleted(userEntity.getIsDeleted())
                .role(roleDto)
                .build()
                ;
    }
}
