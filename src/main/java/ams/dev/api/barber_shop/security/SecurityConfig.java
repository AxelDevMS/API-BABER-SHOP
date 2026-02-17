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
 * Configuración de seguridad de Spring Security para la aplicación.
 *
 * Objetivo:
 * - Definir la cadena de filtros (SecurityFilterChain) y las reglas de autorización.
 * - Proveer beans necesarios para autenticación basada en UserDetails (DAO) y encriptación de contraseñas.
 * - Inyectar un filtro JWT para extracción/validación de tokens antes del procesamiento por defecto.
 *
 * Principales responsabilidades y decisiones de diseño:
 * - Se usa un {@link UserDetailsService} (implementado por {@link UserServiceImpl}) para cargar usuarios desde la capa de dominio/repository.
 * - Se configura un {@link DaoAuthenticationProvider} con un {@link PasswordEncoder} BCrypt para comparación segura de credenciales.
 * - El {@link AuthenticationManager} se construye explícitamente como un {@link ProviderManager} que contiene el proveedor DAO; esto permite inyectar el manager en otras partes de la aplicación si es necesario.
 * - Se añade {@link JwtFilter} antes de {@link UsernamePasswordAuthenticationFilter} para permitir la validación del token JWT en solicitudes entrantes.
 *
 * Notas de seguridad técnicas:
 * - BCrypt es usado para almacenar/validar contraseñas, aprovechando su coste ajustable y salt incorporado.
 * - CSRF se deshabilita en la configuración HTTP: esto es común en APIs REST stateless que usan tokens (si el app es sólo API, asegurarse de usar otras protecciones apropiadas).
 * - Las rutas públicas y privadas se definen en {@link #basicAuth(HttpSecurity)}; revisar este método si la política de endpoints cambia.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    /**
     * Define la cadena de filtros y las reglas de autorización HTTP.
     *
     * Comportamiento principal:
     * - Deshabilita CSRF porque la aplicación expone endpoints REST y usa tokens (stateless).
     * - Declara rutas permitidas sin autenticación y exige autenticación para el resto.
     * - Inserta {@code jwtFilter} antes del filtro de autenticación por nombre de usuario para validar tokens JWT previamente.
     *
     * Consideraciones de implementación:
     * - Si se pretende usar sesiones stateful o formularios, hay que revisar CSRF y configurar session management.
     * - El orden de los filtros importa: colocar el filtro JWT antes de UsernamePasswordAuthenticationFilter permite poblar el SecurityContext desde el token.
     *
     * @param httpSecurity objeto de configuración HTTP de Spring Security
     * @return SecurityFilterChain configurada
     */
    @Bean
    public SecurityFilterChain basicAuth(HttpSecurity httpSecurity) {
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
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    /**
     * Bean que expone la implementación de {@link UserDetailsService} usada por los proveedores de autenticación.
     *
     * Implementación concreta:
     * - Devuelve una instancia de {@link UserServiceImpl} que debe implementar {@code loadUserByUsername}.
     *
     * Contrato:
     * - Input: username (a través del flujo de autenticación Spring Security).
     * - Output: {@code UserDetails} con username, password (hashed) y authorities.
     * - Error mode: lanzar UsernameNotFoundException si el usuario no existe.
     *
     * @return UserDetailsService para cargar usuarios desde la capa de persistencia
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserServiceImpl();
    }

    /**
     * Construye el {@link AuthenticationManager} usando un {@link DaoAuthenticationProvider}.
     *
     * Detalles:
     * - {@code DaoAuthenticationProvider} delega en {@link UserDetailsService} para recuperar el usuario y usa el {@link PasswordEncoder}
     *   para comparar el password proporcionado con el hash almacenado.
     * - Se devuelve un {@link ProviderManager} que contiene el proveedor configurado.
     *
     * Casos de uso:
     * - Este bean puede inyectarse allí donde se necesite iniciar una autenticación programática.
     *
     * @param userDetailsService servicio que devuelve {@code UserDetails}
     * @param passwordEncoder codificador usado para validar contraseñas
     * @return AuthenticationManager configurado con el proveedor DAO
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
     * Provee el {@link PasswordEncoder} para la aplicación.
     *
     * Elegimos {@link BCryptPasswordEncoder} por las siguientes razones técnicas:
     * - Incluye salt por diseño y produce hashes adaptativos (cost factor configurable).
     * - Es resistente a ataques de diccionario/ fuerza bruta cuando el cost es suficientemente alto.
     * - Es ampliamente soportado e integrado en Spring Security.
     *
     * Nota operativa: si se necesita ajustar el coste en producción, pasar el parámetro de strength al constructor.
     *
     * @return implementación BCrypt de PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
