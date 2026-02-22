package ams.dev.api.barber_shop.security.jwt;

import ams.dev.api.barber_shop.enums.RoleEnum;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * FILTRO DE AUTENTICACIÓN JWT (JSON WEB TOKEN)
 * =============================================
 *
 * ¿QUÉ HACE ESTA CLASE?
 * ---------------------
 * Esta clase es un filtro de seguridad personalizado que intercepta TODAS las
 * peticiones HTTP entrantes a la API para validar la autenticación mediante
 * tokens JWT. Actúa como la primera línea de defensa para determinar si un
 * usuario está autenticado o no.
 *
 * PROPÓSITO PRINCIPAL:
 * -------------------
 * Extraer el token JWT del header de la petición, validarlo y establecer el
 * contexto de seguridad de Spring Security para que el usuario autenticado
 * pueda ser identificado en el resto de la aplicación.
 *
 * RESPONSABILIDADES CLAVE:
 * -----------------------
 * 1. INTERCEPTAR PETICIONES:
 *    - Hereda de OncePerRequestFilter para garantizar que se ejecute UNA SOLA VEZ
 *      por cada petición HTTP, evitando procesamiento duplicado.
 *
 * 2. EXTRAER TOKEN JWT:
 *    - Busca el header "Authorization" en la petición HTTP
 *    - Verifica que tenga el formato correcto: "Bearer {token}"
 *    - Extrae la parte del token eliminando el prefijo "Bearer "
 *
 * 3. VALIDAR TOKEN JWT:
 *    - Utiliza JwtService para verificar la firma criptográfica del token
 *    - Extrae todos los claims (información) contenidos en el token
 *    - Verifica que el token NO haya expirado
 *
 * 4. EXTRAER ROL Y PERMISOS:
 *    - Obtiene el rol del usuario desde el claim "Role" del token
 *    - Convierte el rol a un enum RoleEnum para validación
 *    - Construye la lista de autoridades (granted authorities) que incluye:
 *      a) El rol del usuario (ej: ROLE_ADMIN, ROLE_USER)
 *      b) Todos los permisos asociados a ese rol
 *
 * 5. ESTABLECER CONTEXTO DE SEGURIDAD:
 *    - Crea un UsernamePasswordAuthenticationToken con las autoridades del usuario
 *    - Añade detalles de la petición (IP, sesión, etc.)
 *    - Almacena la autenticación en SecurityContextHolder para que Spring Security
 *      reconozca al usuario como autenticado en el hilo actual
 *
 * 6. CONTINUAR CADENA DE FILTROS:
 *    - Siempre permite que la petición continúe (incluso sin token válido)
 *    - Si no hay autenticación, el usuario será tratado como ANÓNIMO
 *    - Los endpoints protegidos rechazarán peticiones no autenticadas
 *
 * FLUJO DE TRABAJO TÍPICO:
 * -----------------------
 * 1. Usuario envía petición a /api/appointments con header:
 *    "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
 *
 * 2. JwtFilter intercepta la petición:
 *    - Extrae el token → "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
 *    - Valida firma con JwtService → OK
 *    - Verifica expiración → NO ha expirado
 *    - Extrae rol → "ROLE_ADMIN" del claim "Role"
 *    - Obtiene permisos del rol → [READ, WRITE, DELETE]
 *
 * 3. Establece autenticación:
 *    - Usuario "admin@example.com" autenticado con rol ADMIN y sus permisos
 *    - Spring Security ahora sabe que el usuario está autenticado
 *
 * 4. La petición continúa al controlador:
 *    - El controlador puede acceder al usuario vía @AuthenticationPrincipal
 *    - Las anotaciones @PreAuthorize evaluarán los permisos correctamente
 *
 * 5. Si el token es INVÁLIDO o NO EXISTE:
 *    - No se establece autenticación
 *    - La petición continúa como usuario ANÓNIMO
 *    - Los endpoints protegidos devolverán 401 UNAUTHORIZED
 *
 * COMPONENTES QUE UTILIZA:
 * -----------------------
 * - JwtService: Servicio para validar tokens y extraer claims
 * - RoleEnum: Enumeración que define los roles y sus permisos asociados
 * - SecurityContextHolder: Almacena el contexto de seguridad del hilo actual
 * - UsernamePasswordAuthenticationToken: Implementación de Authentication de Spring
 *
 * @see ams.dev.api.barber_shop.security.jwt.JwtService
 * @see ams.dev.api.barber_shop.enums.RoleEnum
 * @see org.springframework.security.core.context.SecurityContextHolder
 */
@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired // Inyección automática del servicio JWT para validación y extracción de claims
    private JwtService jwtService;

    /**
     * Método principal del filtro que se ejecuta para cada petición HTTP.
     *
     * @param request La petición HTTP entrante
     * @param response La respuesta HTTP que se enviará
     * @param filterChain La cadena de filtros para continuar el procesamiento
     * @throws ServletException Si ocurre un error en el filtro
     * @throws IOException Si ocurre un error de entrada/salida
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Obtiene el header "Authorization" de la petición HTTP
        // Este header debe contener el token JWT en formato "Bearer {token}"
        String authHeader = request.getHeader("Authorization");
        String token = null;

        // Verifica que el header exista y comience con "Bearer" (con espacio después)
        // Esto asegura que el token tenga el formato esperado por el estándar OAuth2
        if (authHeader != null && authHeader.startsWith("Bearer")){
            // Extrae el token eliminando los primeros 7 caracteres ("Bearer ")
            // Ejemplo: "Bearer eyJhbGci..." → "eyJhbGci..."
            token = authHeader.substring(7);
        }

        // Procesa el token SOLO si:
        // 1. Existe un token en el header
        // 2. No hay una autenticación previa en el contexto de seguridad
        //    (esto evita procesar el token múltiples veces en la misma petición)
        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null){

            // Verifica la firma del token y extrae TODOS los claims (información) que contiene
            // Si el token es inválido (firma incorrecta, malformado, etc.), JwtService lanzará una excepción
            Claims claims = jwtService.verifySignatureAndExtractAllClaims(token);

            // Obtiene el rol del usuario desde el claim personalizado "Role"
            // El formato esperado es el nombre del rol sin prefijo (ej: "ADMIN", "USER")
            // Luego construye el nombre completo del enum: "ROLE_" + valor obtenido
            // Ejemplo: claim "Role": "ADMIN" → RoleEnum.ROLE_ADMIN
            RoleEnum role = RoleEnum.valueOf("ROLE_"+claims.get("Role",String.class));

            // Crea una lista mutable de autoridades (granted authorities) para el usuario
            // Inicializa la lista con el nombre del rol como autoridad
            List<SimpleGrantedAuthority> simpleGrantedAuthorityList = new ArrayList<>(List.of(new SimpleGrantedAuthority(role.name())));

            // Itera sobre TODOS los permisos asociados al rol del usuario
            // Cada permiso se agrega como una autoridad adicional
            // Esto permite control de acceso fino a nivel de métodos con @PreAuthorize
            role.getPermissions().forEach(permission ->{
                simpleGrantedAuthorityList.add(new SimpleGrantedAuthority(permission.name()));
            });

            // Verifica que el token NO haya expirado comparando la fecha actual
            // con el claim "exp" (expiration) del token
            if (!jwtService.isTokenExpired(token)){

                // Crea un token de autenticación de Spring Security
                // Parámetros:
                // - Principal: El subject del token (generalmente el email/username)
                // - Credenciales: null (el token JWT ya fue validado)
                // - Authorities: La lista de roles y permisos del usuario
                UsernamePasswordAuthenticationToken userPassAuthToken = new UsernamePasswordAuthenticationToken(
                        claims.getSubject(), // Extrae el username del claim "sub" (subject)
                        null,                // No se necesitan credenciales adicionales
                        simpleGrantedAuthorityList // Lista de autoridades (rol + permisos)
                );

                // Enriquece el token de autenticación con detalles de la petición HTTP:
                // - Dirección IP del cliente
                // - ID de sesión (si existe)
                // - Otros detalles de la petición web
                // Esto es útil para auditoría y logging
                userPassAuthToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // Establece la autenticación en el contexto de seguridad de Spring
                // A partir de este momento, Spring Security considera al usuario como AUTENTICADO
                // El objeto de autenticación estará disponible en toda la aplicación vía:
                // - SecurityContextHolder.getContext().getAuthentication()
                // - @AuthenticationPrincipal en controladores
                // - expressiones #authentication en vistas/plantillas
                SecurityContextHolder.getContext().setAuthentication(userPassAuthToken);
            }
        }

        // Continúa con el siguiente filtro en la cadena de Spring Security
        // Si no hay más filtros, la petición llegará al controlador correspondiente
        // NOTA: Siempre continuamos la cadena, incluso si:
        // - No hay token
        // - El token es inválido
        // - El token expiró
        // La decisión de permitir o denegar el acceso la tomarán los filtros de autorización posteriores
        filterChain.doFilter(request, response);
    }
}