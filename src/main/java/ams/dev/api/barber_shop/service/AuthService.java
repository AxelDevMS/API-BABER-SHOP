package ams.dev.api.barber_shop.service;

import ams.dev.api.barber_shop.dto.ApiResponseDto;
import ams.dev.api.barber_shop.dto.AuthRequestDto;
import ams.dev.api.barber_shop.dto.UserRequestDto;
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

    ApiResponseDto authenticate(AuthRequestDto authRequestDto);
}
