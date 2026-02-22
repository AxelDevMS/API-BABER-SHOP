package ams.dev.api.barber_shop.service.impl;

import ams.dev.api.barber_shop.dto.*;
import ams.dev.api.barber_shop.entity.PermissionEntity;
import ams.dev.api.barber_shop.entity.RoleEntity;
import ams.dev.api.barber_shop.exceptions.BusinessException;
import ams.dev.api.barber_shop.exceptions.DuplicateResourceException;
import ams.dev.api.barber_shop.exceptions.ResourceNotFoundException;
import ams.dev.api.barber_shop.mapper.request.RoleRequestMapper;
import ams.dev.api.barber_shop.mapper.response.RoleResponseMapper;
import ams.dev.api.barber_shop.repository.RoleRespository;
import ams.dev.api.barber_shop.service.PermissionService;
import ams.dev.api.barber_shop.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRespository roleRespository;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleRequestMapper roleRequestMapper;

    @Autowired
    private RoleResponseMapper roleResponseMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Override
    public ApiResponseDto executeCreateRole(RoleRequestDto roleRequestDto) {
        this.validateDuplicatedRole(roleRequestDto,null);

        List<String> permissionIds = roleRequestDto.getPermissions().stream()
                .map(PermissionRequestDto::getId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        List<PermissionEntity> permissions = this.permissionService.findAllPermissionsIds(permissionIds);


        RoleEntity roleEntity = this.roleRequestMapper.toEntity(roleRequestDto);
        roleEntity.setName(this.normalizeRoleName(roleEntity.getName()));
        roleEntity.setIsDeleted(false);
        roleEntity.setPermissions(permissions);

        roleEntity = this.roleRespository.save(roleEntity);

        return new ApiResponseDto("Rol registrado con éxito con id: " + roleEntity.getId());
    }

    @Override
    public List<RoleResponseDto> executeGetRoles() {
        List<RoleEntity> roleEntities = this.roleRespository.findAll();
        if (roleEntities.isEmpty())
            throw  new ResourceNotFoundException("No hay registros en el sistema");

        return roleEntities.stream()
                .map(role -> {
                    // Obtener permisos para este rol
                    List<PermissionResponseDto> permissions = this.permissionService
                            .findAllPermissionsByRoleId(role.getId());

                    // Convertir rol a DTO con sus permisos
                    return this.roleResponseMapper.toDto(role, permissions);
                })
                .toList();
    }

    @Override
    public RoleResponseDto executeGetRole(String id) {
        this.validateId(id);
        Optional<RoleEntity> role =  this.roleRespository.findById(id);

        List<PermissionResponseDto> permissions = this.permissionService.findAllPermissionsByRoleId(id);

        if (role.isEmpty())
            throw new ResourceNotFoundException("Rol","id", id);


        return this.roleResponseMapper.toDto(role.get(),permissions);
    }

    @Override
    public ApiResponseDto executeUpdateRole(String id, RoleRequestDto roleRequestDto) {
        this.validateId(id);

        RoleEntity role = this.roleRespository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol", "id", id));

        this.validateDuplicatedRole(roleRequestDto,id);

        role.setName(roleRequestDto.getName());
        role.setDescription(roleRequestDto.getDescription());


        List<String> permissionIds = roleRequestDto.getPermissions().stream()
                .map(PermissionRequestDto::getId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        List<PermissionEntity> permissions = this.permissionService.findAllPermissionsIds(permissionIds);

        role.setPermissions(permissions);

        role = this.roleRespository.save(role);

        return new ApiResponseDto("Rol actualizado con éxito con id: " + role.getId());
    }

    @Override
    public RoleEntity findRoleById(String id) {
        return roleRespository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol", "id", id));
    }

    @Override
    public void executeDeleteRole(String id) {
        this.validateId(id);

        RoleEntity role = this.roleRespository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol", "id", id));

        role.setIsDeleted(true);
        this.roleRespository.save(role);
    }

    private void validateId(String id){
        if (id == null || id.trim().isEmpty()) {
            Map<String, List<String>> errors = new HashMap<>();
            errors.put("id", List.of("El ID no puede estar vacío"));
            throw new BusinessException("Error de validación", errors);
        }
    }

    private void validateDuplicatedRole(RoleRequestDto roleDto, String excludeId){
        Optional<RoleEntity> existingRole;

        if (excludeId == null)
            existingRole = roleRespository.findByName(roleDto.getName());
        else
            existingRole = roleRespository.findByNameAndIdNot(roleDto.getName(), excludeId);

        if (existingRole.isPresent())
            throw new DuplicateResourceException("Role", "nombre", roleDto.getName());
    }

    private String normalizeRoleName(String roleName) {
        if (roleName == null || roleName.trim().isEmpty()) {
            return roleName;
        }

        return roleName.trim()
                .toUpperCase()
                .replaceAll("\\s+", "_"); // \\s+ captura uno o más espacios
    }
}
