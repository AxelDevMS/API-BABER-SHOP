package ams.dev.api.barber_shop.service;

import ams.dev.api.barber_shop.dto.ApiResponseDto;
import ams.dev.api.barber_shop.dto.PermissionRequestDto;
import ams.dev.api.barber_shop.dto.PermissionResponseDto;
import jakarta.validation.Valid;

import java.util.List;

public interface PermissionService {
    ApiResponseDto executeCreatePermission(PermissionRequestDto permissionRequestDto);

    PermissionResponseDto executeGetPermission(String id);

    List<PermissionResponseDto> executeGetPermissions();

    ApiResponseDto executeUpdatePermission(String id, @Valid PermissionRequestDto permissionRequestDto);

    void executeDeletePermission(String id);
}
