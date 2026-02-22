package ams.dev.api.barber_shop.service.impl;

import ams.dev.api.barber_shop.dto.ApiResponseDto;
import ams.dev.api.barber_shop.dto.AuthRequestDto;
import ams.dev.api.barber_shop.dto.UserRequestDto;
import ams.dev.api.barber_shop.entity.UserEntity;
import ams.dev.api.barber_shop.mapper.request.UserRequestMapper;
import ams.dev.api.barber_shop.repository.UserRepository;
import ams.dev.api.barber_shop.security.jwt.JwtService;
import ams.dev.api.barber_shop.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * SERVICIO DE IMPLEMENTACIÓN DE USUARIOS
 * ======================================
 *
 * ¿QUÉ HACE ESTA CLASE?
 * ---------------------
 * Esta clase es el corazón de la lógica de negocio relacionada con usuarios en la aplicación.
 * Implementa dos interfaces fundamentales:
 *
 * 1. UserService: Define las operaciones CRUD y de negocio para usuarios
 * 2. UserDetailsService: Interfaz de Spring Security para cargar usuarios durante la autenticación
 *
 * PROPÓSITO PRINCIPAL:
 * -------------------
 * Gestionar todo el ciclo de vida de los usuarios en el sistema, desde su registro
 * hasta su autenticación, actuando como puente entre la capa de persistencia (repositorios)
 * y la capa de presentación (controladores), además de integrarse con Spring Security
 * para la autenticación y autorización.
 *
 * RESPONSABILIDADES CLAVE:
 * -----------------------
 * 1. AUTENTICACIÓN DE USUARIOS:
 *    - Valida credenciales (username/password) usando AuthenticationManager de Spring
 *    - Genera tokens JWT para sesiones exitosas mediante JwtService
 *    - Maneja errores de autenticación (usuario no encontrado, credenciales inválidas)
 *
 * 2. REGISTRO DE USUARIOS:
 *    - Convierte DTOs de entrada a entidades (EmployeeRequestDto → UserEntity)
 *    - Encripta contraseñas de forma segura usando BCrypt ANTES de persistir
 *    - Guarda nuevos usuarios en la base de datos
 *    - Retorna el ID generado (UUID) al cliente
 *
 * 3. CARGA DE USUARIOS PARA SPRING SECURITY:
 *    - Implementa loadUserByUsername() requerido por UserDetailsService
 *    - Consulta la BD por username y estado activo
 *    - Construye objetos UserDetails con:
 *      a) Username
 *      b) Password encriptado
 *      c) Authorities (rol + todos los permisos asociados al rol)
 *    - Este método es INVOCADO AUTOMÁTICAMENTE por Spring Security durante:
 *      - Autenticación HTTP Basic
 *      - Validación de JWT en JwtFilter
 *
 * 4. CONSULTA DE USUARIOS:
 *    - Busca usuarios por username (solo activos)
 *    - Lanza UsernameNotFoundException si no existe (estándar de Spring Security)
 *
 * FLUJO DE TRABAJO TÍPICO:
 * -----------------------
 *
 * CASO 1: REGISTRO DE NUEVO USUARIO (PUBLIC)
 * -----------------------------------------
 * 1. Cliente → POST /user/register → EmployeeRequestDto (username, password, isActive)
 * 2. executeCreateEmployee() recibe el DTO
 * 3. MapperEntity convierte DTO → UserEntity
 * 4. PasswordEncoder encripta la contraseña (BCrypt)
 * 5. UserRepository guarda en BD (genera UUID automático)
 * 6. Retorna ApiResponseDto con ID generado
 *
 * CASO 2: AUTENTICACIÓN (PUBLIC)
 * -----------------------------
 * 1. Cliente → POST /user/login → AuthRequestDto (username, password)
 * 2. authenticate() recibe credenciales
 * 3. AuthenticationManager valida usando loadUserByUsername() (este mismo servicio)
 * 4. Si válido → JwtService genera token JWT con username y rol
 * 5. Retorna ApiResponseDto con token JWT
 *
 * CASO 3: CARGA DE USUARIO POR SPRING SECURITY (INTERNO)
 * -----------------------------------------------------
 * 1. Llamado automáticamente por:
 *    - Filtro HTTP Basic (UsernamePasswordAuthenticationFilter)
 *    - JwtFilter (después de validar token)
 * 2. loadUserByUsername(username) busca en BD
 * 3. Construye UserDetails con:
 *    - Username
 *    - Password encriptado
 *    - Authorities: [ROLE_USER, READ, WRITE] (rol + permisos)
 * 4. Spring Security usa esto para:
 *    - Validar contraseña (HTTP Basic)
 *    - Establecer contexto de seguridad
 *    - Evaluar @PreAuthorize en controladores
 *
 * COMPONENTES QUE UTILIZA:
 * -----------------------
 * - UserRepository: Acceso a base de datos para operaciones CRUD
 * - MapperEntity: Conversión entre DTOs y entidades
 * - PasswordEncoder: Encriptación BCrypt de contraseñas
 * - AuthenticationManager: Gestor de autenticación de Spring
 * - JwtService: Generación de tokens JWT
 *
 * @see AuthService
 * @see org.springframework.security.core.userdetails.UserDetailsService
 * @see ams.dev.api.barber_shop.repository.UserRepository
 * @see ams.dev.api.barber_shop.security.jwt.JwtService
 */
@Service
public class AuthServiceImpl implements AuthService, UserDetailsService {

    @Autowired // Inyección del repositorio JPA para operaciones de base de datos con usuarios
    private UserRepository userRepository;

    @Autowired // Inyección del mapper para convertir entre DTOs y entidades
    private UserRequestMapper userRequestMapper;

    @Autowired // Inyección del encoder para encriptar contraseñas (BCryptPasswordEncoder)
    private PasswordEncoder passwordEncoder;

    @Autowired // Inyección del AuthenticationManager de Spring para validar credenciales
    @Lazy // Lazy loading para evitar dependencias circulares con SecurityConfig
    private AuthenticationManager authenticationManager;

    @Autowired // Inyección del servicio JWT para generar tokens después de autenticación exitosa
    private JwtService jwtService;

    /**
     * AUTENTICA UN USUARIO Y GENERA UN TOKEN JWT
     * ==========================================
     *
     * Este método maneja el login de usuarios, validando credenciales y generando
     * un token JWT para sesiones autenticadas.
     *
     * FLUJO DETALLADO:
     * ----------------
     * 1. Recibe AuthRequestDto con username y password (texto plano)
     * 2. Crea UsernamePasswordAuthenticationToken con las credenciales
     * 3. AuthenticationManager.authenticate() valida:
     *    a) Llama a loadUserByUsername() para obtener UserDetails
     *    b) Compara password ingresado con hash almacenado usando PasswordEncoder
     *    c) Verifica si la cuenta está activa/habilitada
     * 4. Si autenticación es exitosa:
     *    a) Extrae el primer authority (rol) de la lista
     *    b) Limpia el prefijo "ROLE_" para obtener el nombre base del rol
     *    c) Genera token JWT con username y rol
     *    d) Retorna ApiResponseDto con el token
     * 5. Si falla: Retorna mensaje de error genérico
     *
     * EXCEPCIONES QUE PUEDE LANZAR (manejadas por Spring Security):
     * ------------------------------------------------------------
     * - UsernameNotFoundException: Usuario no existe en BD
     * - BadCredentialsException: Contraseña incorrecta
     * - DisabledException: Usuario deshabilitado (isActive = false)
     * - LockedException: Cuenta bloqueada
     *
     * @param authRequestDto DTO con credenciales: username y password (sin encriptar)
     * @return ApiResponseDto con:
     *         - Éxito: "Token generado: {jwt_token}"
     *         - Error: "No se pudo generar el token"
     */
    @Override
    public ApiResponseDto authenticate(AuthRequestDto authRequestDto) {
        // Intenta autenticar al usuario con las credenciales proporcionadas
        // authenticationManager.authenticate() delegará en loadUserByUsername() automáticamente
        Authentication  authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequestDto.getUsername(), // Principal: username
                        authRequestDto.getPassword()  // Credenciales: password en texto plano
                )
        );

        // Verifica si la autenticación fue exitosa
        if (authenticate.isAuthenticated()){
            // Obtiene el primer authority de la lista (asumimos que es el rol)
            // Ejemplo: "ROLE_ADMIN" → se limpia para obtener "ADMIN"
            String role = authenticate
                    .getAuthorities() // Obtiene Collection<? extends GrantedAuthority>
                    .iterator() // Obtiene un iterador sobre las autoridades
                    .next() // Toma la primera autoridad (el rol)
                    .getAuthority() // Obtiene el string de la autoridad (ej: "ROLE_ADMIN")
                    .replace("ROLE_",""); // Elimina el prefijo "ROLE_" para usar en el token

            // Genera token JWT con username y rol (sin prefijo)
            String token = jwtService.generateToken(authRequestDto.getUsername(), role);

            // Retorna respuesta exitosa con el token
            return new ApiResponseDto("Token generado: "+token);
        }

        // Si la autenticación falló por cualquier razón
        return new ApiResponseDto("No se pudo generar el token");
    }

    /**
     * CREA UN NUEVO EMPLEADO/USUARIO EN EL SISTEMA
     * ============================================
     *
     * Este método maneja el registro de nuevos usuarios, asegurando que las
     * contraseñas se almacenen de forma segura.
     *
     * FLUJO DETALLADO:
     * ----------------
     * 1. Recibe EmployeeRequestDto con datos del nuevo usuario
     * 2. MapperEntity convierte DTO → UserEntity (mapea campos básicos)
     * 3. Encripta la contraseña usando BCrypt ANTES de guardar
     * 4. UserRepository.save() persiste la entidad:
     *    - Genera automáticamente UUID como ID
     *    - Establece timestamps de creación/actualización
     * 5. Retorna ApiResponseDto con el ID generado
     *
     * CONSIDERACIONES DE SEGURIDAD:
     * ----------------------------
     * - La contraseña NUNCA se guarda en texto plano
     * - BCrypt genera un salt aleatorio automáticamente
     * - El factor de costo (strength) es configurable desde PasswordEncoder
     * - El ID es UUID no secuencial (evita enumeración de usuarios)
     *
     * @param empolyeeRequestDto DTO con datos:
     *                           - username: String único
     *                           - password: String sin encriptar
     *                           - isActive: Boolean (true/false)
     * @return ApiResponseDto con mensaje: "Empleado Registrado Correctamente con id: {uuid}"
     */
    @Override
    public ApiResponseDto executeCreateEmployee(UserRequestDto empolyeeRequestDto) {
        // PASO 1: Mapear DTO a entidad
        // Convierte EmployeeRequestDto → UserEntity (copia username y isActive)
        UserEntity userEntity = userRequestMapper.toEntity(empolyeeRequestDto);

        // PASO 2: Encriptar contraseña
        // Toma la contraseña en texto plano del DTO, la encripta y la asigna a la entidad
        userEntity.setPassword(passwordEncoder.encode(empolyeeRequestDto.getPassword()));

        // PASO 3: Persistir en base de datos
        // save() inserta el registro y retorna la entidad con el ID generado
        userEntity = userRepository.save(userEntity);

        // PASO 4: Retornar respuesta con el ID generado
        // El ID es un UUID autogenerado por JPA/Hibernate
        return new ApiResponseDto("Empleado Registrado Correctamente con id: " + userEntity.getId());
    }

    /**
     * BUSCA UN USUARIO ACTIVO POR NOMBRE DE USUARIO
     * =============================================
     *
     * Método auxiliar para obtener usuarios de la BD con validación de existencia.
     *
     * FLUJO DETALLADO:
     * ----------------
     * 1. Busca en BD por username Y isActive = true
     * 2. Si encuentra → retorna UserEntity
     * 3. Si NO encuentra → lanza UsernameNotFoundException
     *
     * USO PRINCIPAL:
     * -------------
     * - loadUserByUsername() lo usa para cargar usuarios
     * - Otros servicios pueden usarlo para validar existencia
     *
     * @param username nombre de usuario a buscar (case-sensitive)
     * @return UserEntity usuario activo encontrado
     * @throws UsernameNotFoundException si no existe usuario activo con ese username
     */
    @Override
    public UserEntity getUserByUsername(String username) {
        // Busca en repositorio por username y isActive=true
        // orElseThrow: si el Optional está vacío, lanza la excepción
        return userRepository.findByUsernameAndIsActive(username, true)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    /**
     * CARGA DETALLES DE USUARIO PARA SPRING SECURITY
     * ==============================================
     *
     * Este método es el corazón de la integración con Spring Security.
     * Es INVOCADO AUTOMÁTICAMENTE por el framework durante:
     *
     * 1. Autenticación HTTP Basic:
     *    - UsernamePasswordAuthenticationFilter extrae credenciales del header
     *    - Llama a loadUserByUsername() para obtener UserDetails
     *    - Compara la contraseña del request con la almacenada
     *
     * 2. Validación de JWT:
     *    - JwtFilter extrae username del token
     *    - Llama a loadUserByUsername() para cargar el usuario completo
     *    - Establece la autenticación en SecurityContext
     *
     * CONSTRUCCIÓN DE UserDetails:
     * ---------------------------
     * - username: El identificador único del usuario
     * - password: El hash BCrypt almacenado en BD
     * - authorities: Lista completa de:
     *   a) El rol del usuario (ej: ROLE_ADMIN)
     *   b) TODOS los permisos asociados a ese rol (ej: READ, WRITE, DELETE)
     *
     * Esto permite:
     * - Control de acceso a nivel de método con @PreAuthorize
     * - Validación de permisos específicos en controladores/servicios
     * - Auditoría y logging de acciones por usuario
     *
     * @param username nombre de usuario a cargar (desde request o token)
     * @return UserDetails objeto con todos los datos para autenticación/autorización
     * @throws UsernameNotFoundException si el usuario no existe o no está activo
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Obtiene la entidad del usuario desde BD (o lanza excepción si no existe)
        UserEntity userEntity = getUserByUsername(username);

        // Construye lista de autoridades (GrantedAuthority) para el usuario
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        // 1. Agrega el ROL como autoridad (ej: "ROLE_ADMIN")
        authorities.add(new SimpleGrantedAuthority(userEntity.getRole().getName()));

        // 2. Agrega TODOS los PERMISOS asociados al rol como autoridades individuales
        //    Esto permite control granular con @PreAuthorize("hasAuthority('READ')")
        userEntity.getRole().getPermissions().forEach(permissionEnum -> {
            authorities.add(new SimpleGrantedAuthority(permissionEnum.getName()));
        });

        // Construye y retorna UserDetails usando el builder de Spring Security
        return User
                .builder() // User.builder() de org.springframework.security.core.userdetails.User
                .username(userEntity.getUsername()) // Nombre de usuario
                .password(userEntity.getPassword()) // Password YA encriptado (BCrypt)
                .authorities(authorities) // Lista de roles + permisos
                .build(); // Construye el objeto UserDetails
    }
}