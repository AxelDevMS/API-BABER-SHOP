package ams.dev.api.barber_shop.security;

import ams.dev.api.barber_shop.security.jwt.JwtFilter;
import ams.dev.api.barber_shop.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * CONFIGURACIÓN CENTRAL DE SEGURIDAD DE LA APLICACIÓN
 * ===================================================
 *
 * ¿QUÉ HACE ESTA CLASE?
 * ---------------------
 * Esta clase es el corazón de la seguridad de la aplicación. Configura y define
 * cómo se protegen los endpoints, cómo se autentican los usuarios y cómo se
 * manejan los tokens JWT en el sistema.
 *
 * PROPÓSITO PRINCIPAL:
 * -------------------
 * Actuar como la capa de seguridad que protege todos los recursos de la API,
 * asegurando que solo usuarios autenticados y autorizados puedan acceder a
 * las funcionalidades del sistema.
 *
 * RESPONSABILIDADES CLAVE:
 * -----------------------
 * 1. DEFINIR REGLAS DE ACCESO:
 *    - Endpoints públicos: /user/** (registro, login) - accesibles sin token
 *    - Endpoints privados: Todo lo demás - requieren autenticación JWT
 *
 * 2. CONFIGURAR EL FILTRO JWT:
 *    - Intercepta todas las peticiones entrantes
 *    - Extrae y valida el token JWT del header Authorization
 *    - Establece el contexto de seguridad si el token es válido
 *
 * 3. GESTIONAR LA AUTENTICACIÓN:
 *    - Define cómo se cargan los usuarios desde la BD (UserDetailsService)
 *    - Configura el algoritmo de hashing de contraseñas (BCrypt)
 *    - Provee el AuthenticationManager para autenticación programática
 *
 * 4. DESHABILITAR PROTECCIONES INNECESARIAS:
 *    - CSRF desactivado porque la app es stateless (sin sesiones)
 *    - La autenticación es token-based, no por cookies de sesión
 *
 * FLUJO DE TRABAJO TÍPICO:
 * -----------------------
 * 1. Usuario no autenticado → POST /user/register → Público
 * 2. Usuario no autenticado → POST /user/login → Público → Recibe JWT
 * 3. Usuario autenticado → GET /appointments (con JWT en header) → Filtro JWT valida → Acceso concedido
 * 4. Usuario no autenticado → GET /appointments → Filtro JWT no encuentra token → Acceso denegado (401)
 *
 * COMPONENTES QUE INTEGRA:
 * -----------------------
 * - JwtFilter: Filtro personalizado para validar tokens JWT
 * - UserServiceImpl: Servicio que carga usuarios desde la base de datos
 * - BCryptPasswordEncoder: Algoritmo para hashing seguro de contraseñas
 * - SecurityFilterChain: Cadena de filtros de Spring Security
 *
 * @see ams.dev.api.barber_shop.security.jwt.JwtFilter
 * @see ams.dev.api.barber_shop.service.impl.UserServiceImpl
 * @see org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired // Inyección de dependencia automática del filtro JWT personalizado
    private JwtFilter jwtFilter;

    /**
     * Configura la cadena de filtros de seguridad principal.
     * Define reglas de autorización, deshabilita CSRF y posiciona el filtro JWT.
     *
     * @param httpSecurity El builder de HttpSecurity proporcionado por Spring Security
     * @return SecurityFilterChain La cadena de filtros construida y compilada
     */
    @Bean // Marca el método como productor de un bean gestionado por Spring
    public SecurityFilterChain basicAuth(HttpSecurity httpSecurity) {
        // Configuración fluida de HttpSecurity
        httpSecurity
                .csrf(csrf -> csrf.disable()) // Deshabilita CSRF (Cross-Site Request Forgery) porque la app es stateless y usa JWT
                .authorizeHttpRequests(auth -> // Configura la autorización para peticiones HTTP
                        auth
                                .requestMatchers("/user/**") // Define patrones de URL específicos
                                .permitAll() // Permite acceso sin autenticación a todas las rutas que comiencen con /user/**
                                .anyRequest() // Cualquier otra petición no cubierta por reglas anteriores
                                .authenticated() // Requiere que el usuario esté autenticado
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // Añade el filtro JWT personalizado ANTES del filtro de autenticación estándar de Spring

        return httpSecurity.build(); // Construye y retorna la SecurityFilterChain
    }

    /**
     * Expone el servicio de detalles de usuario como bean.
     * Esta implementación concreta carga usuarios desde la base de datos.
     *
     * @return UserDetailsService Implementación concreta que busca usuarios en la BD
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserServiceImpl(); // Instancia y retorna el servicio personalizado que implementa UserDetailsService
    }

    /**
     * Configura y expone el AuthenticationManager como bean.
     * Utiliza DaoAuthenticationProvider con UserDetailsService y PasswordEncoder.
     *
     * @param userDetailsService Servicio para cargar usuarios (se inyecta automáticamente)
     * @param passwordEncoder Codificador de contraseñas (se inyecta automáticamente)
     * @return AuthenticationManager Gestor de autenticación configurado
     */
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        // Crea un proveedor de autenticación DAO que usa UserDetailsService para cargar usuarios
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);

        // Configura el codificador de contraseñas para que el proveedor pueda validar credenciales
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);

        // Crea y retorna un ProviderManager que delega la autenticación en el DaoAuthenticationProvider
        return new ProviderManager(daoAuthenticationProvider);
    }

    /**
     * Expone el codificador de contraseñas como bean.
     * Utiliza BCrypt con factor de costo por defecto (10).
     *
     * @return PasswordEncoder Implementación BCrypt para hashing seguro de contraseñas
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Instancia el encoder BCrypt con strength por defecto (10)
    }
}