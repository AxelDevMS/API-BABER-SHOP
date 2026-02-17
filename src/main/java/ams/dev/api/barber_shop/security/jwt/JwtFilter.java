package ams.dev.api.barber_shop.security.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Filtro de autenticación basado en JWT (JSON Web Token) para la arquitectura de seguridad de Spring Security.
 *
 * <p>Esta clase implementa un filtro que se ejecuta una única vez por solicitud HTTP ({@link OncePerRequestFilter})
 * y es responsable de:
 * <ul>
 *   <li>Extraer el token JWT del encabezado Authorization de la solicitud HTTP</li>
 *   <li>Validar la integridad y firma del token JWT</li>
 *   <li>Verificar que el token no haya expirado</li>
 *   <li>Establecer el contexto de seguridad de Spring Security con la autenticación del usuario</li>
 * </ul>
 * </p>
 *
 * <p><strong>Flujo de procesamiento:</strong></p>
 * <ol>
 *   <li>Extrae el token del encabezado "Authorization" esperando el formato "Bearer {token}"</li>
 *   <li>Si existe un token y no hay autenticación previa en el contexto de seguridad:
 *       <ul>
 *           <li>Verifica la firma y extrae los claims (información) del token</li>
 *           <li>Valida que el token no esté expirado</li>
 *           <li>Si es válido, crea un {@link UsernamePasswordAuthenticationToken} con el nombre del usuario</li>
 *           <li>Establece los detalles de autenticación web (dirección IP, session ID, etc.)</li>
 *           <li>Asigna la autenticación al {@link SecurityContextHolder} para el hilo actual</li>
 *       </ul>
 *   </li>
 *   <li>Continúa la cadena de filtros con la siguiente solicitud</li>
 * </ol>
 *
 * @author Sistema de Autenticación
 * @version 1.0
 * @since 1.0
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    /**
     * Servicio de validación y procesamiento de tokens JWT.
     * Inyectado automáticamente por Spring Framework mediante autowiring.
     */
    @Autowired
    private JwtService jwtService;

    /**
     * Método de filtrado que se ejecuta para cada solicitud HTTP de forma singleton por solicitud.
     *
     * <p>Extrae, valida y procesa el token JWT para autenticar al usuario en el contexto de seguridad
     * de Spring Security antes de que la solicitud llegue al controlador.</p>
     *
     * @param request  {@link HttpServletRequest} - La solicitud HTTP que contiene el token JWT en el encabezado Authorization
     * @param response {@link HttpServletResponse} - La respuesta HTTP
     * @param filterChain {@link FilterChain} - La cadena de filtros de Spring Security para continuar el procesamiento
     *
     * @throws ServletException Si ocurre un error específico del servlet durante el filtrado
     * @throws IOException Si ocurre un error de entrada/salida durante el filtrado
     *
     * @apiNote El token debe estar en el formato: "Authorization: Bearer {jwt_token}"
     * @apiNote Si el token es inválido o ha expirado, la solicitud continúa sin autenticación
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Extrae el encabezado Authorization de la solicitud HTTP
        String authHeader = request.getHeader("Authorization");
        String token = null;

        // Valida el formato "Bearer {token}" y extrae el token eliminando los primeros 7 caracteres ("Bearer ")
        if (authHeader != null && authHeader.startsWith("Bearer")){
            token = authHeader.substring(7);
        }

        // Procesa el token solo si existe y no hay una autenticación previa en el contexto de seguridad
        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null){

            // Verifica la firma RSA/HMAC del token y extrae todos los claims (información codificada)
            Claims claims = jwtService.verifySignatureAndExtractAllClaims(token);

            // Valida que el token no haya sobrepasado su fecha de expiración
            if (!jwtService.isTokenExpired(token)){

                // Crea un token de autenticación de Spring Security con el nombre de usuario extraído del claim 'subject'
                // Los permisos se establecen como una lista vacía ya que la autorización se maneja por separado
                UsernamePasswordAuthenticationToken userPassAuthToken = new UsernamePasswordAuthenticationToken(
                        claims.getSubject(), // Principal: nombre de usuario extraído del JWT
                        null,                // Credenciales: null porque el JWT es la credencial
                        new ArrayList<>()    // Authorities/Permisos: vacío, se pueden agregar según la lógica de negocio
                );

                // Establece detalles adicionales de autenticación web como:
                // - Dirección IP del cliente (remoteAddress)
                // - ID de sesión (sessionId)
                // - Agente de usuario, etc.
                userPassAuthToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // Almacena la autenticación en el contexto de seguridad de Spring para el hilo actual
                // Esto permite que los controladores y servicios accedan al usuario autenticado
                SecurityContextHolder.getContext().setAuthentication(userPassAuthToken);
            }
        }

        // Continúa la cadena de filtros hacia el siguiente filtro o hacia el controlador destino
        filterChain.doFilter(request, response);
    }
}
