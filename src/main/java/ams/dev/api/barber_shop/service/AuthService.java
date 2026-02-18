package ams.dev.api.barber_shop.service;

import ams.dev.api.barber_shop.dto.ApiResponseDto;
import ams.dev.api.barber_shop.dto.AuthRequestDto;
import ams.dev.api.barber_shop.dto.user.UserRequestDto;
import ams.dev.api.barber_shop.entity.UserEntity;
import ams.dev.api.barber_shop.service.impl.AuthServiceImpl;

/**
 * Interfaz que define el contrato de operaciones de negocio para Usuarios.
 *
 * Esta interfaz establece los métodos que deben implementarse para:
 * - Gestión de usuarios/empleados (creación, búsqueda)
 * - Autenticación y autorización
 *
 * Implementación:
 * @see AuthServiceImpl
 */
public interface AuthService {

    /**
     * Obtiene un usuario de la base de datos por su nombre de usuario.
     *
     * Criterios de búsqueda:
     * - Username debe coincidir exactamente
     * - Usuario debe estar activo (isActive = true)
     *
     * @param username nombre de usuario a buscar
     * @return UserEntity encontrado
     * @throws org.springframework.security.core.userdetails.UsernameNotFoundException
     *         si no existe usuario activo con ese username
     */
    UserEntity getUserByUsername(String username);

    /**
     * Crea un nuevo empleado/usuario en el sistema.
     *
     * Flujo técnico:
     * 1. Mapea el DTO a entidad UserEntity
     * 2. Encripta la contraseña usando BCryptPasswordEncoder
     * 3. Persiste la entidad en la base de datos
     * 4. Retorna un DTO de respuesta con mensaje de éxito e ID generado
     *
     * Seguridad:
     * - La contraseña se encripta antes de guardar en BD
     * - Se genera un ID único (UUID) automáticamente
     *
     * @param empolyeeRequestDto DTO con datos del empleado (username, password, isActive)
     * @return ApiResponseDto con mensaje de confirmación y ID del usuario creado
     */
    ApiResponseDto executeCreateEmployee(UserRequestDto empolyeeRequestDto);
    ApiResponseDto authenticate(AuthRequestDto authRequestDto);
}
