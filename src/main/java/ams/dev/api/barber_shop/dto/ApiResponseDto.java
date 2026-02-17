package ams.dev.api.barber_shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) para respuestas genéricas de la API.
 *
 * Este DTO se utiliza para enviar respuestas estandarizadas desde los endpoints REST
 * a los clientes, particularmente para operaciones que no requieren retornar datos complejos.
 *
 * Estructura:
 * - message: Mensaje descriptivo de la operación realizada
 *
 * Casos de uso:
 * - Confirmación de creación de usuario: "Empleado Registrado Correctamente con id: {uuid}"
 * - Mensajes de éxito/error generales
 *
 * Ejemplo de respuesta JSON:
 * {
 *   "message": "Empleado Registrado Correctamente con id: 550e8400-e29b-41d4-a716-446655440000"
 * }
 *
 * Anotaciones Lombok:
 * - @Data: Genera getters, setters, equals(), hashCode(), toString()
 * - @AllArgsConstructor: Constructor con parámetro de mensaje
 * - @NoArgsConstructor: Constructor sin parámetros (deserialización JSON)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDto {

    /**
     * Mensaje descriptivo de la respuesta de la API.
     *
     * Puede contener:
     * - Mensajes de éxito
     * - Información de recursos creados (incluir IDs)
     * - Mensajes informativos
     */
    private String message;
}
