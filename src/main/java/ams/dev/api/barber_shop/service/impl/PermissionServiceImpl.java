package ams.dev.api.barber_shop.service.impl;

import ams.dev.api.barber_shop.dto.ApiResponseDto;
import ams.dev.api.barber_shop.dto.PermissionRequestDto;
import ams.dev.api.barber_shop.dto.PermissionResponseDto;
import ams.dev.api.barber_shop.entity.PermissionEntity;
import ams.dev.api.barber_shop.exceptions.BusinessException;
import ams.dev.api.barber_shop.exceptions.DuplicateResourceException;
import ams.dev.api.barber_shop.exceptions.ResourceNotFoundException;
import ams.dev.api.barber_shop.mapper.MapperDto;
import ams.dev.api.barber_shop.mapper.MapperEntity;
import ams.dev.api.barber_shop.repository.PermissionRepository;
import ams.dev.api.barber_shop.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private MapperEntity mapperEntity;

    @Autowired
    private MapperDto mapperDto;

    @Override
    public ApiResponseDto executeCreatePermission(PermissionRequestDto permissionRequestDto) {
        // Validar duplicados antes de crear
        validateDuplicatePermission(permissionRequestDto, null);

        PermissionEntity permissionSaved = this.mapperEntity.toPermission(permissionRequestDto);
        permissionSaved = this.permissionRepository.save(permissionSaved);

        return new ApiResponseDto("Permiso registrado con éxito con id: " + permissionSaved.getId());
    }

    @Override
    public PermissionResponseDto executeGetPermission(String id) {
        validateId(id);
        Optional<PermissionEntity> permission = this.permissionRepository.findById(id);

        if (permission.isEmpty())
            throw new ResourceNotFoundException("Permiso","id", id);

        return this.mapperDto.toPermission(permission.get());
    }

    @Override
    public List<PermissionResponseDto> executeGetPermissions() {
        List<PermissionEntity> permissionEntityList = this.permissionRepository.findAll();
        if (permissionEntityList.isEmpty())
            throw new ResourceNotFoundException("No hay registros en el sistema");

        return permissionEntityList.stream()
                .map(permissionEntity -> this.mapperDto.toPermission(permissionEntity))
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
        validateId(id);

        PermissionEntity permission = this.permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permiso", "id", id));

        this.permissionRepository.delete(permission);
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
