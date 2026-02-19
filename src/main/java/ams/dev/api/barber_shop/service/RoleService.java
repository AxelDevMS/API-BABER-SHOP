package ams.dev.api.barber_shop.service;

import ams.dev.api.barber_shop.dto.ApiResponseDto;
import ams.dev.api.barber_shop.dto.RoleRequestDto;
import ams.dev.api.barber_shop.dto.RoleResponseDto;
import jakarta.validation.Valid;

import java.util.List;

public interface RoleService {
    ApiResponseDto executeCreateRole(@Valid RoleRequestDto roleRequestDto);

    List<RoleResponseDto> executeGetRoles();

    RoleResponseDto executeGetRole(String id);

    ApiResponseDto executeUpdateRole(String id, @Valid RoleRequestDto roleRequestDto);

    void executeDeleteRole(String id);
}
