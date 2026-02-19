package ams.dev.api.barber_shop.dto;

import lombok.Data;

/**
 * DTO (Data Transfer Object) para recibir datos de creación de empleado.
 *
 * Este DTO se utiliza para recibir información de un nuevo empleado/usuario desde
 * las solicitudes HTTP del cliente. Los datos son validados y mapeados a la entidad
 * correspondiente antes de persistirse en la base de datos.
 *
 * Flujo de uso:
 * 1. Cliente envía JSON con estos datos al endpoint POST /user/create
 * 2. Spring deserializa el JSON a esta clase
 * 3. UserController recibe el DTO y lo pasa a UserService
 * 4. UserService mapea a UserEntity y persiste en BD
 *
 * Ejemplo de solicitud JSON:
 * {
 *   "username": "juanperez",
 *   "password": "mi_contraseña_123",
 *   "isActive": true
 * }
 *
 * Anotación Lombok:
 * - @Data: Genera getters, setters, equals(), hashCode(), toString()
 */
@Data
public class UserRequestDto {

    private String username;

    private String password;

    private Boolean isActive;

    private String role;
}
