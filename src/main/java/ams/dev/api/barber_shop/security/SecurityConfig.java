package ams.dev.api.barber_shop.security;

import ams.dev.api.barber_shop.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad de la aplicación usando Spring Security.
 *
 * Esta clase centraliza toda la configuración de seguridad incluyendo:
 * - Autenticación HTTP Basic
 * - Autorización y control de acceso a endpoints
 * - Encriptación de contraseñas
 * - Definición de beans de seguridad
 *
 * Anotaciones:
 * - @Configuration: Marca como clase de configuración Spring
 * - @EnableWebSecurity: Activa la seguridad web de Spring
 *
 * Flujo de autenticación HTTP Basic:
 * 1. Cliente envía header: Authorization: Basic base64(username:password)
 * 2. Spring Security extrae credentials
 * 3. Llama AuthenticationManager.authenticate()
 * 4. Valida contra UserDetailsService (UserServiceImpl)
 * 5. Compara contraseña usando PasswordEncoder (BCrypt)
 * 6. Si es válido, permite acceso; si no, retorna 401 Unauthorized
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configura la cadena de filtros de seguridad para HTTP.
     *
     * Define qué endpoints requieren autenticación y cuáles son públicos:
     *
     * Endpoints permitidos (sin autenticación):
     * - GET /hello
     * - GET /hey
     * - GET /hi
     * - Cualquier ruta bajo /user/**
     *
     * Endpoints que requieren autenticación:
     * - Todos los demás (anyRequest().authenticated())
     *
     * Autenticación habilitada:
     * - HTTP Basic con valores por defecto
     *
     * Flujo de una solicitud:
     * 1. Spring Security intercepta la solicitud
     * 2. Verifica si la ruta está permitida
     * 3. Si no, requiere autenticación HTTP Basic
     * 4. Si la ruta está permitida, permite acceso sin autenticación
     *
     * @param httpSecurity objeto para configurar seguridad HTTP
     * @return SecurityFilterChain configurada
     * @throws Exception si hay error en la configuración
     */
    @Bean
    public SecurityFilterChain basicAuth(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                // Configuración de autorización por ruta
                .authorizeHttpRequests(auth ->
                        auth
                        // Permite acceso público a /hello
                        .requestMatchers("/hello")
                            .permitAll()
                        // Permite acceso público a todas las rutas bajo /user/**
                        .requestMatchers("/user/**")
                            .permitAll()
                        // Requiere autenticación para cualquier otra ruta
                        .anyRequest().authenticated()
                )
                // Habilita autenticación HTTP Basic con configuración por defecto
                .httpBasic(Customizer.withDefaults());

        return httpSecurity.build();
    }

    /**
     * Bean que proporciona el servicio de detalles de usuario.
     *
     * Este bean es utilizado por Spring Security para:
     * 1. Cargar detalles del usuario durante autenticación
     * 2. Validar que el usuario existe
     * 3. Obtener detalles como roles y permisos
     *
     * Implementación: UserServiceImpl que implementa UserDetailsService
     * Método clave: loadUserByUsername() que busca el usuario en BD
     *
     * @return UserDetailsService implementado por UserServiceImpl
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserServiceImpl();
    }

    /**
     * Bean que proporciona el AuthenticationManager.
     *
     * El AuthenticationManager es responsable de:
     * 1. Recibir las credenciales (username y password)
     * 2. Seleccionar el proveedor de autenticación apropiado
     * 3. Delegar la validación al proveedor
     * 4. Retornar el Authentication si es válido
     *
     * Implementación: ProviderManager que usa DaoAuthenticationProvider
     *
     * Flujo:
     * 1. DaoAuthenticationProvider usa UserDetailsService para cargar usuario
     * 2. Compara la contraseña proporcionada con la almacenada
     * 3. Usa PasswordEncoder para comparación segura
     * 4. Retorna Authentication autenticado o lanza AuthenticationException
     *
     * @param userDetailsService servicio que carga detalles del usuario
     * @param passwordEncoder codificador de contraseñas
     * @return AuthenticationManager configurado
     */
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        // Crear proveedor DAO que valida contra UserDetailsService
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);

        // Asignar el encoder para comparar contraseñas
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);

        // Crear manager que utiliza este proveedor
        return new ProviderManager(daoAuthenticationProvider);
    }

    /**
     * Bean que proporciona el codificador de contraseñas.
     *
     * BCryptPasswordEncoder es el estándar de seguridad moderna para contraseñas:
     *
     * Características:
     * - Usa el algoritmo bcrypt (basado en Blowfish)
     * - Genera salt aleatorio automáticamente
     * - Es computacionalmente costoso (slow by design) para resistir ataques de fuerza bruta
     * - Es unidireccional: no se pueden desencriptar, solo se comparan
     * - Incluye costo adaptable (strength)
     *
     * Flujo de encriptación:
     * 1. Se genera un salt aleatorio
     * 2. Se aplica bcrypt con el salt
     * 3. Se almacena: $2a$10$...(salt + hash)
     *
     * Flujo de validación:
     * 1. Se extrae el salt del hash almacenado
     * 2. Se aplica bcrypt a la contraseña con ese salt
     * 3. Se compara el resultado con el hash almacenado
     *
     * @return BCryptPasswordEncoder para uso en toda la aplicación
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
