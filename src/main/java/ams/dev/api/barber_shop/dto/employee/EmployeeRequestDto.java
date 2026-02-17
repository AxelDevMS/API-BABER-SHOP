package ams.dev.api.barber_shop.dto.employee;

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
public class EmployeeRequestDto {

    /**
     * Nombre de usuario único del empleado.
     *
     * Requisitos:
     * - Debe ser único en el sistema
     * - Se utiliza para autenticación (login)
     * - No contiene espacios en blanco
     *
     * Ejemplo: "carlos_barbero", "empleado_001"
     */
    private String username;

    /**
     * Contraseña del empleado en texto plano.
     *
     * Importante:
     * - Se recibe SIN encriptar desde el cliente
     * - Se encripta en el servidor ANTES de guardar en BD
     * - Nunca se almacena en texto plano
     * - Se recomienda usar HTTPS para transmisión segura
     *
     * Requisitos sugeridos:
     * - Mínimo 8 caracteres
     * - Debe contener mayúsculas, minúsculas, números
     * - Se pueden agregar validaciones en futuro
     */
    private String password;

    /**
     * Estado de activación del empleado.
     *
     * Valores:
     * - true: Empleado activo, puede usar la aplicación
     * - false: Empleado inactivo, no puede autenticarse
     *
     * Utilidad:
     * - Permite dar de baja lógica a empleados sin eliminar datos
     * - Los usuarios inactivos se filtra en búsquedas
     *
     * Ejemplo: true (nuevo empleado activo)
     */
    private Boolean isActive;
}
