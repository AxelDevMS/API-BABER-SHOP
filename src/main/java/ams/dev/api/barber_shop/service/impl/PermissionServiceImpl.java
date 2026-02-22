package ams.dev.api.barber_shop.service.impl;

import ams.dev.api.barber_shop.dto.ApiResponseDto;
import ams.dev.api.barber_shop.dto.PermissionRequestDto;
import ams.dev.api.barber_shop.dto.PermissionResponseDto;
import ams.dev.api.barber_shop.entity.PermissionEntity;
import ams.dev.api.barber_shop.entity.RoleEntity;
import ams.dev.api.barber_shop.exceptions.BusinessException;
import ams.dev.api.barber_shop.exceptions.DuplicateResourceException;
import ams.dev.api.barber_shop.exceptions.ResourceNotFoundException;
import ams.dev.api.barber_shop.mapper.request.PermissionRequestMapper;
import ams.dev.api.barber_shop.mapper.response.PermissionResponseMapper;
import ams.dev.api.barber_shop.projection.PermissionBasicProjection;
import ams.dev.api.barber_shop.repository.PermissionRepository;
import ams.dev.api.barber_shop.service.PermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PermissionRequestMapper permissionRequestMapper;

    @Autowired
    private PermissionResponseMapper permissionResponseMapper;

    private static  final Logger LOGGER = LoggerFactory.getLogger(PermissionServiceImpl.class);

    @Override
    public ApiResponseDto executeCreatePermission(PermissionRequestDto permissionRequestDto) {
        // Validar duplicados antes de crear
        validateDuplicatePermission(permissionRequestDto, null);

        PermissionEntity permissionSaved = this.permissionRequestMapper.toEntity(permissionRequestDto);
        permissionSaved.setIsDeleted(false);
        permissionSaved.setIsActive(true);
        permissionSaved = this.permissionRepository.save(permissionSaved);

        return new ApiResponseDto("Permiso registrado con éxito con id: " + permissionSaved.getId());
    }

    @Override
    public PermissionResponseDto executeGetPermission(String id) {
        validateId(id);
        Optional<PermissionEntity> permission = this.permissionRepository.findById(id);

        if (permission.isEmpty())
            throw new ResourceNotFoundException("Permiso","id", id);

        return this.permissionResponseMapper.toDto(permission.get());
    }

    @Override
    public List<PermissionResponseDto> executeGetPermissions() {
        List<PermissionEntity> permissionEntityList = this.permissionRepository.findAll();
        if (permissionEntityList.isEmpty())
            throw new ResourceNotFoundException("No hay registros en el sistema");

        return permissionEntityList.stream()
                .map(permissionEntity -> this.permissionResponseMapper.toDto(permissionEntity))
                .toList();
    }

    @Override
    public ApiResponseDto executeUpdatePermission(String id, PermissionRequestDto permissionRequestDto) {
        validateId(id);

        PermissionEntity permissionToUpdate = this.permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permiso", "id", id));

        // Validar duplicados excluyendo el permiso actual
        validateDuplicatePermission(permissionRequestDto, id);

        permissionToUpdate.setName(permissionRequestDto.getName());
        permissionToUpdate.setModule(permissionRequestDto.getModule());
        permissionToUpdate.setAction(permissionRequestDto.getAction());
        permissionToUpdate.setDescription(permissionRequestDto.getDescription());

        this.permissionRepository.save(permissionToUpdate);

        return new ApiResponseDto("Permiso actualizado con éxito con id: " + id);
    }

    @Override
    public void executeDeletePermission(String id) {
        this.validateId(id);

        PermissionEntity permission = this.permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol", "id", id));

        permission.setIsDeleted(true);

        this.permissionRepository.save(permission);
    }

    @Override
    public List<PermissionEntity> findAllPermissionsIds(List<String> ids) {
        List<PermissionEntity> permissions = this.permissionRepository.findAllByIdIn(ids);

        if (permissions.isEmpty())
            throw new ResourceNotFoundException("No se encontraron permisos para los IDs proporcionados");

        return permissions;
    }

    @Override
    public List<PermissionResponseDto> findAllPermissionsByRoleId(String roleId) {
        return this.permissionRepository.findBasicDtoByRoleId(roleId);
    }

    // Métodos privados de validación
    private void validateId(String id) {
        if (id == null || id.trim().isEmpty()) {
            Map<String, List<String>> errors = new HashMap<>();
            errors.put("id", List.of("El ID no puede estar vacío"));
            throw new BusinessException("Error de validación", errors);
        }
    }

    private void validateDuplicatePermission(PermissionRequestDto request, String excludeId) {
        // Verificar duplicado por nombre (asumiendo que el nombre debe ser único)
        Optional<PermissionEntity> existingPermission;

        if (excludeId == null) {
            existingPermission = permissionRepository.findByName(request.getName());
        } else {
            existingPermission = permissionRepository.findByNameAndIdNot(request.getName(), excludeId);
        }

        if (existingPermission.isPresent()) {
            throw new DuplicateResourceException("Permiso", "nombre", request.getName());
        }
    }





}
