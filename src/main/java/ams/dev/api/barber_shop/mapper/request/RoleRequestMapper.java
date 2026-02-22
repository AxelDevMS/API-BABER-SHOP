package ams.dev.api.barber_shop.mapper.request;

import ams.dev.api.barber_shop.dto.RoleRequestDto;
import ams.dev.api.barber_shop.entity.PermissionEntity;
import ams.dev.api.barber_shop.entity.RoleEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleRequestMapper {

    public RoleEntity toEntity(RoleRequestDto roleRequestDto){

        if (roleRequestDto == null) return null;

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(roleRequestDto.getName());
        roleEntity.setDescription(roleRequestDto.getDescription());
        return roleEntity;
    }


    public RoleEntity toRole(RoleRequestDto roleRequestDto, List<PermissionEntity> permissionList){

        if (roleRequestDto == null) return null;

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(roleRequestDto.getName());
        roleEntity.setDescription(roleRequestDto.getDescription());
        roleEntity.setPermissions(permissionList);
        return roleEntity;
    }
}
