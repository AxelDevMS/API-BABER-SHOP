package ams.dev.api.barber_shop.service;

import ams.dev.api.barber_shop.dto.ApiResponseDto;
import ams.dev.api.barber_shop.dto.PermissionRequestDto;
import ams.dev.api.barber_shop.dto.PermissionResponseDto;
import ams.dev.api.barber_shop.entity.PermissionEntity;
import ams.dev.api.barber_shop.entity.RoleEntity;
import ams.dev.api.barber_shop.projection.PermissionBasicProjection;
import jakarta.validation.Valid;

import java.util.List;

public interface PermissionService {
    ApiResponseDto executeCreatePermission(PermissionRequestDto permissionRequestDto);

    PermissionResponseDto executeGetPermission(String id);

    List<PermissionResponseDto> executeGetPermissions();

    ApiResponseDto executeUpdatePermission(String id, @Valid PermissionRequestDto permissionRequestDto);

    void executeDeletePermission(String id);

    List<PermissionEntity> findAllPermissionsIds(List<String> ids);

    List<PermissionResponseDto> findAllPermissionsByRoleId(String roleId);

}
