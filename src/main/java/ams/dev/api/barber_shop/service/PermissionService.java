package ams.dev.api.barber_shop.service;

import ams.dev.api.barber_shop.dto.ApiResponseDto;
import ams.dev.api.barber_shop.dto.permission.PermissionFilterDto;
import ams.dev.api.barber_shop.dto.permission.PermissionRequestDto;
import ams.dev.api.barber_shop.dto.permission.PermissionResponseDto;
import ams.dev.api.barber_shop.dto.pagination.PageResponseDto;
import ams.dev.api.barber_shop.entity.PermissionEntity;
import jakarta.validation.Valid;

import java.util.List;

public interface PermissionService {
    ApiResponseDto executeCreatePermission(PermissionRequestDto permissionRequestDto);

    PermissionResponseDto executeGetPermission(String id);

    List<PermissionResponseDto> executeGetPermissions();

    PageResponseDto<PermissionResponseDto> executeGetListPermission(PermissionFilterDto paramDto);

    ApiResponseDto executeUpdatePermission(String id, @Valid PermissionRequestDto permissionRequestDto);

    void executeDeletePermission(String id);

    List<PermissionEntity> findAllPermissionsIds(List<String> ids);

    List<PermissionResponseDto> findAllPermissionsByRoleId(String roleId);

}
