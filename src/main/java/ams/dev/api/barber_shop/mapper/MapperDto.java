package ams.dev.api.barber_shop.mapper;

import ams.dev.api.barber_shop.dto.PermissionResponseDto;
import ams.dev.api.barber_shop.entity.PermissionEntity;
import org.springframework.stereotype.Component;

@Component
public class MapperDto {

    public PermissionResponseDto toPermission(PermissionEntity permissionEntity){
        PermissionResponseDto permissionResponseDto = new PermissionResponseDto();
        permissionResponseDto.setId(permissionEntity.getId());
        permissionResponseDto.setName(permissionEntity.getName());
        permissionResponseDto.setModule(permissionEntity.getModule());
        permissionResponseDto.setAction(permissionEntity.getAction());
        permissionResponseDto.setDescription(permissionEntity.getDescription());
        permissionResponseDto.setCreatedAt(permissionEntity.getCreatedAt());
        permissionResponseDto.setUpdatedAt(permissionEntity.getUpdatedAt());
        return permissionResponseDto;
    }
}
