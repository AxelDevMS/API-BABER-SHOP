package ams.dev.api.barber_shop.entity;

import ams.dev.api.barber_shop.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad JPA que representa un Usuario en la base de datos.
 *
 * Esta clase mapea la tabla "users" en la base de datos relacional.
 * Utiliza generación de IDs de tipo UUID (Universal Unique Identifier) para garantizar
 * unicidad a nivel global sin depender de auto-incrementos secuenciales.
 *
 * Anotaciones Lombok utilizadas:
 * - @Data: Genera getters, setters, equals(), hashCode(), toString()
 * - @AllArgsConstructor: Genera constructor con todos los parámetros
 * - @NoArgsConstructor: Genera constructor sin parámetros (requerido por JPA)
 *
 * Tabla en BD:
 * - Nombre: "users"
 * - Columnas:
 *   * id (UUID, clave primaria)
 *   * username (String, identificador único del usuario)
 *   * password (String, contraseña encriptada con BCrypt)
 *   * is_active (Boolean, indica si el usuario está activo)
 */
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserEntity {

    /**
     * Identificador único del usuario.
     *
     * Estrategia: UUID (Universal Unique Identifier)
     * - Ventajas: Unicidad global, no requiere coordinación de IDs
     * - Generado automáticamente por JPA
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    /**
     * Nombre de usuario único para autenticación.
     *
     * Se utiliza como credencial para iniciar sesión en el sistema.
     * Debe ser único en la base de datos (aunque no está configurado como unique constraint).
     */
    @Column(name = "username")
    private String username;

    /**
     * Contraseña del usuario encriptada.
     *
     * Se almacena siempre encriptada utilizando BCryptPasswordEncoder.
     * Nunca debe almacenarse en texto plano por razones de seguridad.
     *
     * @see org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
     */
    @Column(name = "password")
    private String password;

    /**
     * Estado de activación del usuario.
     *
     * - true: Usuario activo, puede autenticarse
     * - false: Usuario inactivo, no puede autenticarse (lógica soft-delete)
     *
     * Se utiliza en las búsquedas para filtrar solo usuarios activos
     * (@see UserRepository#findByUsernameAndIsActive)
     */
    @Column(name = "is_active")
    private Boolean isActive;


    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private RoleEnum role;
}
