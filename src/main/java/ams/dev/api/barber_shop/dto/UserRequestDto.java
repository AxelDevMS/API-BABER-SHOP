package ams.dev.api.barber_shop.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRequestDto {

    @NotNull(message = "El nombre no puede ser nulo")
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotNull(message = "El apellido no puede ser nulo")
    @NotBlank(message = "El apellido es obligatorio")
    private String lastName;

    @NotNull(message = "El teléfono no puede ser nulo")
    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(
            regexp = "^\\+?[0-9]{1,3}[\\s.-]?[0-9]{1,4}[\\s.-]?[0-9]{1,4}[\\s.-]?[0-9]{1,9}$",
            message = "El teléfono debe tener un formato válido (ej: +1234567890, 123-456-7890, etc)"
    )
    private String phone;

    @NotNull(message = "El nombre de usuario no puede ser nulo")
    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;

    @NotNull(message = "La contraseña es obligatoria")
    @NotBlank(message = "La contraseña es obligatoria")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "La contraseña debe tener al menos 8 caracteres, incluyendo mayúsculas, minúsculas, números y caracteres especiales (@$!%*?&)"
    )
    private String password;

    @NotNull(message = "La confirmación de contraseña es obligatoria")
    @NotBlank(message = "La confirmación de contraseña es obligatoria")
    private String confirmPassword;

    private String email;

    private RoleRequestDto role;
}
