package ams.dev.api.barber_shop.mapper.response;

import ams.dev.api.barber_shop.dto.PermissionRequestDto;
import ams.dev.api.barber_shop.dto.PermissionResponseDto;
import ams.dev.api.barber_shop.dto.RoleResponseDto;
import ams.dev.api.barber_shop.entity.PermissionEntity;
import ams.dev.api.barber_shop.entity.RoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleResponseMapper {

    @Autowired
    private PermissionResponseMapper permissionResponseMapper;

    public RoleResponseDto toDto(RoleEntity roleEntity){
        RoleResponseDto roleResponseDto = new RoleResponseDto();
        roleResponseDto.setId(roleEntity.getId());
        roleResponseDto.setName(roleEntity.getName());
        roleResponseDto.setDescription(roleEntity.getDescription());
        roleResponseDto.setIsDeleted(roleEntity.getIsDeleted());
        roleResponseDto.setCreatedAt(roleEntity.getCreatedAt());
        roleResponseDto.setUpdatedAt(roleEntity.getUpdatedAt());
        return roleResponseDto;
    }

    public RoleResponseDto toDto(RoleEntity roleEntity, List<PermissionResponseDto> permissionResponseDtos){
        RoleResponseDto roleResponseDto = new RoleResponseDto();
        roleResponseDto.setId(roleEntity.getId());
        roleResponseDto.setName(roleEntity.getName());
        roleResponseDto.setDescription(roleEntity.getDescription());
        roleResponseDto.setIsDeleted(roleEntity.getIsDeleted());
        roleResponseDto.setCreatedAt(roleEntity.getCreatedAt());
        roleResponseDto.setUpdatedAt(roleEntity.getUpdatedAt());
        roleResponseDto.setPermissions(permissionResponseDtos);


        return roleResponseDto;
    }



}
