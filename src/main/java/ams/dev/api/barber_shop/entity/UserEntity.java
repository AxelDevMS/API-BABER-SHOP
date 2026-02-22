package ams.dev.api.barber_shop.entity;

import ams.dev.api.barber_shop.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

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

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "username")
    private String username;

    private String email;

    private String password;

    private String name;

    @Column(name = "lastname")
    private String lastName;

    private String phone;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "last_login")
    private Date lastLogin;

    //@Column(name = "role")
    //@Enumerated(EnumType.STRING)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private RoleEntity role;

    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private List<BarberShopEntity> barberShops;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;


}
