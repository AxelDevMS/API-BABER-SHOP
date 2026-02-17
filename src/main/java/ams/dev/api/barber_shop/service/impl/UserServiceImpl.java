package ams.dev.api.barber_shop.service.impl;

import ams.dev.api.barber_shop.dto.ApiResponseDto;
import ams.dev.api.barber_shop.dto.AuthRequestDto;
import ams.dev.api.barber_shop.dto.employee.EmployeeRequestDto;
import ams.dev.api.barber_shop.entity.UserEntity;
import ams.dev.api.barber_shop.mapper.MapperEntity;
import ams.dev.api.barber_shop.repository.UserRepository;
import ams.dev.api.barber_shop.security.jwt.JwtService;
import ams.dev.api.barber_shop.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Implementación de la lógica de negocio para operaciones de Usuario.
 *
 * Esta clase implementa dos interfaces principales:
 *
 * 1. UserService: Define operaciones CRUD de usuarios
 * 2. UserDetailsService: Requerido por Spring Security para cargar detalles de usuario
 *    durante la autenticación HTTP Basic
 *
 * Responsabilidades:
 * - Crear nuevos usuarios con contraseña encriptada
 * - Recuperar usuarios por username desde la BD
 * - Proporcionar UserDetails para Spring Security
 *
 * Dependencias inyectadas:
 * - UserRepository: Acceso a datos de usuarios
 * - MapperEntity: Conversión DTO -> Entidad
 * - PasswordEncoder: Encriptación de contraseñas (BCrypt)
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    /**
     * Repositorio JPA para operaciones de persistencia de usuarios
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Mapper para convertir DTOs a entidades y viceversa
     */
    @Autowired
    private MapperEntity mapper;

    /**
     * Encoder para encriptar contraseñas de forma segura
     * Implementación: BCryptPasswordEncoder
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;


    /**
     * Autentica un usuario y genera un token JWT si las credenciales son válidas.
     *
     * Flujo de ejecución:
     * 1. Crea un token de autenticación con username y password proporcionados
     * 2. Valida las credenciales contra la BD usando AuthenticationManager
     * 3. Si la autenticación es exitosa, genera un token JWT
     * 4. Retorna respuesta con el token o mensaje de error
     *
     * Detalles técnicos:
     * - Se usa UsernamePasswordAuthenticationToken para encapsular las credenciales
     * - AuthenticationManager valida contra UserDetailsService (este mismo servicio)
     * - El token JWT se genera usando JwtService con el username autenticado
     * - AuthenticationManager puede lanzar excepción si credenciales son inválidas
     *
     * Casos de éxito:
     * - Username y password válidos → Token generado exitosamente
     *
     * Casos de error:
     * - Username no existe o no está activo → UsernameNotFoundException
     * - Password incorrecto → BadCredentialsException
     * - Usuario deshabilitado → DisabledException
     *
     * @param authRequestDto DTO con username y password sin encriptar
     * @return ApiResponseDto con mensaje de éxito incluyendo el token JWT,
     *         o mensaje de error si la autenticación falló
     * @throws UsernameNotFoundException si el usuario no existe o está inactivo
     */
    @Override
    public ApiResponseDto authenticate(AuthRequestDto authRequestDto) {
        Authentication  authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequestDto.getUsername(),
                        authRequestDto.getPassword()
                )
        );

        if (authenticate.isAuthenticated()){
            String token = jwtService.generateToken(authRequestDto.getUsername());
            return new ApiResponseDto("Token generado: "+token);
        }
        return new ApiResponseDto("No se pudo generar el token");
    }

    /**
     * Crea un nuevo empleado/usuario en el sistema.
     *
     * Flujo de ejecución:
     * 1. Convierte EmployeeRequestDto a UserEntity usando el mapper
     * 2. Encripta la contraseña en texto plano usando BCryptPasswordEncoder
     * 3. Persiste la entidad en la base de datos
     * 4. Retorna un DTO con mensaje de éxito e ID generado (UUID)
     *
     * Consideraciones de seguridad:
     * - La contraseña se encripta ANTES de guardar en BD
     * - Se usa BCrypt con salt aleatorio para mayor seguridad
     * - El ID se genera automáticamente como UUID
     *
     * @param empolyeeRequestDto DTO con datos: username, password (sin encriptar), isActive
     * @return ApiResponseDto con mensaje "Empleado Registrado Correctamente con id: {uuid}"
     */
    @Override
    public ApiResponseDto executeCreateEmployee(EmployeeRequestDto empolyeeRequestDto) {
        // Mapear DTO a entidad
        UserEntity userEntity = mapper.toUser(empolyeeRequestDto);

        // Encriptar la contraseña antes de guardar
        userEntity.setPassword(passwordEncoder.encode(empolyeeRequestDto.getPassword()));

        // Persistir en base de datos (genera ID automáticamente)
        userEntity = userRepository.save(userEntity);

        // Retornar respuesta con ID generado
        return new ApiResponseDto("Empleado Registrado Correctamente con id: " + userEntity.getId());
    }

    /**
     * Busca un usuario activo por su nombre de usuario.
     *
     * Flujo de ejecución:
     * 1. Consulta la BD por username E isActive = true
     * 2. Si no encuentra el usuario, lanza UsernameNotFoundException
     * 3. Si encuentra, retorna la entidad
     *
     * Esta excepción es capturada por Spring Security durante la autenticación.
     *
     * @param username nombre de usuario a buscar
     * @return UserEntity del usuario activo
     * @throws UsernameNotFoundException si no existe usuario activo con ese username
     */
    @Override
    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsernameAndIsActive(username, true)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    /**
     * Carga los detalles de un usuario para Spring Security durante la autenticación.
     *
     * Este método es llamado por Spring Security durante el proceso de autenticación HTTP Basic:
     * 1. Extrae el username del header Authorization de la solicitud
     * 2. Llama este método para cargar los detalles del usuario
     * 3. Spring Security compara la contraseña del request con la almacenada
     *
     * Construcción de UserDetails:
     * - username: del usuario buscado
     * - password: contraseña encriptada (Spring Security la compara con la del request)
     * - authorities: lista vacía (sin roles/permisos específicos)
     *
     * @param username nombre de usuario a cargar
     * @return UserDetails con credenciales para validar en Spring Security
     * @throws UsernameNotFoundException si el usuario no existe o no está activo
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = getUserByUsername(username);

        return User
                .builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .authorities(Collections.emptyList())
                .build();
    }
}
