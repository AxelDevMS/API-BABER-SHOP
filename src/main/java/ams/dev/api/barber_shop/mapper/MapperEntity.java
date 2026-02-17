package ams.dev.api.barber_shop.mapper;

import ams.dev.api.barber_shop.dto.employee.EmployeeRequestDto;
import ams.dev.api.barber_shop.entity.UserEntity;
import org.springframework.stereotype.Component;

/**
 * Componente mapper responsable de convertir entre DTOs y entidades JPA.
 *
 * Este mapper implementa el patrón de diseño "Mapper" para desacoplar la capa de
 * presentación (DTOs) de la capa de persistencia (entidades JPA).
 *
 * Beneficios:
 * - Separación de responsabilidades
 * - Facilita cambios en una capa sin afectar la otra
 * - Permite transformar datos durante la conversión
 *
 * En futuro, se podría utilizar MapStruct o ModelMapper para automatizar estas conversiones.
 *
 * Conversiones implementadas:
 * - EmployeeRequestDto -> UserEntity
 */
@Component
public class MapperEntity {

    /**
     * Convierte un DTO de solicitud de empleado a una entidad de usuario.
     *
     * Flujo de conversión:
     * 1. Crea una nueva instancia de UserEntity
     * 2. Copia los campos correspondientes del DTO:
     *    - DTO.username -> Entity.username
     *    - DTO.password -> Entity.password
     *    - DTO.isActive -> Entity.isActive
     * 3. El ID se genera automáticamente en BD (UUID)
     *
     * Importante:
     * - La contraseña se copia sin encriptar (se encripta después en UserService)
     * - El ID no se asigna aquí, se genera al guardar en BD
     * - Se copia el estado de activación tal cual
     *
     * @param employeeRequestDto DTO con datos del formulario de creación de empleado
     * @return UserEntity nueva instancia con datos copiados del DTO
     */
    public UserEntity toUser(EmployeeRequestDto employeeRequestDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(employeeRequestDto.getUsername());
        userEntity.setPassword(employeeRequestDto.getPassword());
        userEntity.setIsActive(employeeRequestDto.getIsActive());
        return userEntity;
    }

}
