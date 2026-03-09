package ams.dev.api.barber_shop.service;

import ams.dev.api.barber_shop.dto.ApiResponseDto;
import ams.dev.api.barber_shop.dto.pagination.PageResponseDto;
import ams.dev.api.barber_shop.dto.role.RoleFilerDto;
import ams.dev.api.barber_shop.dto.role.RoleRequestDto;
import ams.dev.api.barber_shop.dto.role.RoleResponseDto;
import ams.dev.api.barber_shop.entity.RoleEntity;
import jakarta.validation.Valid;

import java.util.List;

public interface RoleService {
    ApiResponseDto executeCreateRole(@Valid RoleRequestDto roleRequestDto);

    List<RoleResponseDto> executeGetRoles();

    PageResponseDto<RoleResponseDto> executeGetListRole(RoleFilerDto queryParamsDto);

    RoleResponseDto executeGetRole(String id);

    ApiResponseDto executeUpdateRole(String id, @Valid RoleRequestDto roleRequestDto);

    RoleEntity findRoleById(String id);

    RoleEntity findByName(String name);

    void executeDeleteRole(String id);
}
