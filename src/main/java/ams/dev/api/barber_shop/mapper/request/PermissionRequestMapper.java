package ams.dev.api.barber_shop.mapper.request;

import ams.dev.api.barber_shop.dto.PermissionRequestDto;
import ams.dev.api.barber_shop.entity.PermissionEntity;
import org.springframework.stereotype.Component;

@Component
public class PermissionRequestMapper {

    public PermissionEntity toEntity(PermissionRequestDto permissionRequestDto){
        PermissionEntity permissionEntity = new PermissionEntity();
        permissionEntity.setName(permissionRequestDto.getName());
        permissionEntity.setModule(permissionRequestDto.getModule());
        permissionEntity.setAction(permissionRequestDto.getAction());
        permissionEntity.setDescription(permissionRequestDto.getDescription());
        return permissionEntity;
    }


}
