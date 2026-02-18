package ams.dev.api.barber_shop.security.jwt;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;

/**
 * Servicio responsable de generar y validar tokens JWT para la aplicación.
 */
@Service // Anotación de Spring que registra esta clase como un bean en el contexto de la aplicación
public class JwtService {

    // Constante que contiene la clave secreta para firmar los tokens
    // Formato actual: Representación hexadecimal de 64 caracteres
    // WARNING: En producción esto debería venir de variables de entorno o un secrets manager
    public static final String SECRET = "cfe2e7df460bf68cbde50fb23f0b4961523e49fc51cd0f3d9d69971d5a4e960f";

    /**
     * Genera un nuevo token JWT para un usuario específico con su rol.
     *
     * @param username El nombre del usuario que será el subject del token
     * @param role El rol del usuario que se añadirá como claim personalizado
     * @return String El token JWT generado en formato compacto
     */
    public String generateToken(String username, String role) {
        // Crea un HashMap vacío para almacenar los claims personalizados del token
        HashMap<String, Object> claims = new HashMap<>();

        // Añade el rol del usuario al mapa de claims con la clave "Role"
        claims.put("Role", role);

        // Construye el token JWT usando el builder pattern de Jwts
        return Jwts.builder() // Inicia la construcción del token
                .setSubject(username) // Establece el subject (usuario) del token
                .setIssuedAt(new Date()) // Establece la fecha de emisión como el momento actual
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // Establece expiración: actual + 30 minutos (1000ms * 60s * 30min)
                .addClaims(claims) // Añade los claims personalizados (el rol) al payload
                .signWith(getSignedKey(), SignatureAlgorithm.HS256) // Firma el token con la clave secreta usando algoritmo HS256
                .compact(); // Convierte el builder en un String JWT compacto (formato base64url)
    }

    /**
     * Obtiene la clave criptográfica para firmar/verificar tokens.
     * Convierte el SECRET en una Key de Java usando el método de jjwt.
     *
     * @return Key Objeto Key listo para usar en operaciones de firma/verificación
     */
    private Key getSignedKey() {
        // Convierte el String SECRET a bytes usando el encoding por defecto de la plataforma
        // y crea una clave HMAC adecuada para HS256
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    /**
     * Verifica la firma de un token y extrae todos sus claims.
     * Este método valida que el token sea válido (firma correcta, no expirado, bien formado).
     *
     * @param token El token JWT a verificar
     * @return Claims Objeto con todos los claims contenidos en el token
     */
    public Claims verifySignatureAndExtractAllClaims(String token) {
        // Construye un parser, establece la clave de verificación, parsea el token y obtiene el body
        return Jwts.parserBuilder() // Inicia la construcción del parser
                .setSigningKey(getSignedKey()) // Establece la misma clave secreta para verificar la firma
                .build() // Construye el parser
                .parseClaimsJws(token) // Parsea el token y valida la firma (lanza excepción si es inválido)
                .getBody(); // Extrae y retorna el cuerpo (claims) del token
    }

    /**
     * Extrae el nombre de usuario (subject) del token.
     *
     * @param token El token JWT
     * @return String El username contenido en el subject del token
     */
    public String extractUsername(String token) {
        // Llama al método de verificación y extracción y obtiene el subject
        return verifySignatureAndExtractAllClaims(token) // Obtiene todos los claims del token
                .getSubject(); // Extrae específicamente el claim "sub" (subject)
    }

    /**
     * Obtiene la fecha de expiración del token.
     *
     * @param token El token JWT
     * @return Date La fecha de expiración contenida en el claim "exp"
     */
    public Date getExpiration(String token) {
        // Extrae todos los claims y obtiene el de expiración
        return verifySignatureAndExtractAllClaims(token) // Obtiene todos los claims del token
                .getExpiration(); // Extrae específicamente el claim "exp" (expiration)
    }

    /**
     * Verifica si el token ha expirado.
     *
     * @param token El token JWT
     * @return boolean true si el token está expirado, false si aún es válido
     */
    public boolean isTokenExpired(String token) {
        // Compara la fecha de expiración con la fecha actual
        return getExpiration(token) // Obtiene la fecha de expiración del token
                .before(new Date()); // Retorna true si la expiración es anterior a ahora (está expirado)
    }
}