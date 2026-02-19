package ams.dev.api.barber_shop.mapper;

import ams.dev.api.barber_shop.dto.PermissionRequestDto;
import ams.dev.api.barber_shop.dto.RoleRequestDto;
import ams.dev.api.barber_shop.dto.UserRequestDto;
import ams.dev.api.barber_shop.entity.PermissionEntity;
import ams.dev.api.barber_shop.entity.RoleEntity;
import ams.dev.api.barber_shop.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.List;

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

    /*
    ===============================================================
        MÉTODO PARA CONVERTIR DE DTO A ENTITY PARA PERMISSION
    ===============================================================
     */

    public PermissionEntity toPermission(PermissionRequestDto permissionRequestDto){
        PermissionEntity permissionEntity = new PermissionEntity();
        permissionEntity.setName(permissionRequestDto.getName());
        permissionEntity.setModule(permissionRequestDto.getModule());
        permissionEntity.setAction(permissionRequestDto.getAction());
        permissionEntity.setDescription(permissionRequestDto.getDescription());
        return permissionEntity;
    }

        /*
    ===============================================================
        MÉTODO PARA CONVERTIR DE DTO A ENTITY PARA ROLES
    ===============================================================
     */
    public RoleEntity toRole(RoleRequestDto roleRequestDto){
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(roleRequestDto.getName());
        roleEntity.setDescription(roleRequestDto.getDescription());

        List<PermissionEntity> permissionEntities = roleRequestDto.getPermissions().stream()
                .map(this::toPermission)
                .toList();

        roleEntity.setPermissions(permissionEntities);
        return roleEntity;
    }


}
