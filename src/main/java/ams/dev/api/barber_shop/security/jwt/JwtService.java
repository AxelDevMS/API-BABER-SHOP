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
 *
 * Notas técnicas y de seguridad:
 * - Usa algoritmo HMAC-SHA256 (HS256) para firmar los tokens.
 * - La clave secreta se define en la constante {@link #SECRET}. En entornos reales
 *   esta clave NO debería estar hardcodeada: debe provenir de un vault/variable
 *   de entorno o configuración cifrada.
 * - {@code Keys.hmacShaKeyFor(byte[])} espera bytes de la clave. Si la clave se
 *   encuentra en formato hex/Base64 se debe decodificar apropiadamente antes de
 *   llamar a ese método. Actualmente se usa {@code SECRET.getBytes()}.
 * - Los métodos de verificación lanzarán excepciones de la librería jjwt
 *   (por ejemplo, JwtException) si la firma es inválida o el token está malformado.
 * - El servicio es stateless y thread-safe ya que no mantiene estado mutable.
 */
@Service
public class JwtService {

    /**
     * Clave secreta usada para firmar los JWTs.
     *
     * Observaciones:
     * - Esta constante contiene un valor en texto (actualmente un string hex-like).
     *   Usar {@code SECRET.getBytes()} convierte el texto a bytes ASCII/UTF-8 — esto
     *   es distinto a decodificar un valor hex o Base64. Asegúrate que el formato
     *   de la clave sea el esperado por tu política de seguridad.
     * - Para HS256 se recomienda una clave de al menos 256 bits (32 bytes). El valor
     *   actual tiene 64 caracteres hex, pero como aquí se usan los bytes del string,
     *   la longitud resultante en bytes puede ser distinta. Mejor práctica: almacenar
     *   la clave en Base64 y decodificarla explicitamente.
     */
    public static final String SECRET = "cfe2e7df460bf68cbde50fb23f0b4961523e49fc51cd0f3d9d69971d5a4e960f";

    /**
     * Genera un JWT firmado conteniendo el nombre de usuario como subject.
     *
     * Comportamiento:
     * - Establece la fecha de emisión (iat) y la expiración (exp). Actualmente la
     *   expiración está fijada a 30 minutos (1000 * 60 * 30 ms).
     * - Añade un mapa de claims vacío. Si necesitas claims adicionales (roles,
     *   permisos, etc.) agrégalos aquí.
     * - Firma el token con HS256 usando la clave retornada por {@link #getSignedKey()}.
     *
     * Consideraciones de seguridad / diseño:
     * - Tamaño de expiración: 30 minutos es razonable para tokens de acceso; si
     *   necesitas refresh tokens implementa un flujo separado.
     * - No incluir información sensible en los claims sin cifrarla.
     *
     * @param username nombre de usuario que se insertará como subject del token
     * @return token JWT compacto (String)
     */
    public String generateToken(String username){
        return Jwts.builder().
                setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*30))
                .addClaims(new HashMap<>())
                .signWith(getSignedKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Construye la clave secreta utilizada por jjwt para firmar y verificar.
     *
     * Detalles:
     * - Utiliza {@link Keys#hmacShaKeyFor(byte[])} que valida/normaliza la clave
     *   internamente.
     * - Actualmente la implementación usa {@code SECRET.getBytes()}; si la clave está
     *   codificada en Base64 o Hex, debe decodificarse explícitamente antes de
     *   pasársela a este método (ej: {@code Base64.getDecoder().decode(secret)}).
     *
     * @return instancia de {@link Key} adecuada para HS256
     */
    private Key getSignedKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    /**
     * Verifica la firma de un token JWT y extrae todos los claims si la verificación
     * es exitosa.
     *
     * Comportamiento / errores:
     * - Si el token está mal formado, la firma es inválida o está expirado, jjwt
     *   lanzará una excepción (p. ej. JwtException o alguna subclase). El llamador
     *   debe manejar esas excepciones y mapearlas a respuestas HTTP apropiadas (401/403).
     *
     * @param token JWT en formato compact
     * @return claims extraídos del token
     */
    public Claims verifySignatureAndExtractAllClaims(String token) {
        return Jwts.
                parserBuilder()
                .setSigningKey(getSignedKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extrae el nombre de usuario (subject) desde el token.
     *
     * @param token JWT válido
     * @return subject (username) presente en el token
     */
    public String extractUsername(String token){
        return verifySignatureAndExtractAllClaims(token)
                .getSubject();
    }

    /**
     * Obtiene la fecha de expiración (claim exp) del token.
     *
     * Nota: si el token está expirado, este método retornará la fecha pasada; la
     * verificación de expiración debe ser realizada por {@link #isTokenExpired(String)}
     * o por el manejador de excepciones de jjwt.
     *
     * @param token JWT
     * @return fecha de expiración contenida en el token
     */
    public Date getExpiration(String token){
        return verifySignatureAndExtractAllClaims(token).getExpiration();
    }

    /**
     * Indica si el token se encuentra expirado comparando el claim exp con la fecha actual.
     *
     * @param token JWT
     * @return true si la fecha de expiración es anterior a ahora
     */
    public boolean isTokenExpired(String token){
        return  getExpiration(token).before(new Date());
    }
}
