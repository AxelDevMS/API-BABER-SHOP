package ams.dev.api.barber_shop.dto.client;

import ams.dev.api.barber_shop.dto.BarberShopResponseDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Datos para crear/actualizar un cliente")
public class ClientRequestDto implements Serializable {

    @Schema(description = "Nombre completo del cliente", example = "Juan Pérez")
    @NotNull(message = "El nombre no puede ser nulo")
    @NotBlank(message = "El nombre es obligatorio")
    private String fullName;

    @Schema(description = "Teléfono del cliente", example = "+521234567890")
    @NotNull(message = "El teléfono no puede ser nulo")
    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(
            regexp = "^\\+?[0-9]{1,3}[\\s.-]?[0-9]{1,4}[\\s.-]?[0-9]{1,4}[\\s.-]?[0-9]{1,9}$",
            message = "El teléfono debe tener un formato válido (ej: +1234567890, 123-456-7890, etc)"
    )
    private String phone;

    @Schema(description = "Email del cliente", example = "juan@email.com")
    @Email(message = "El correo electrónico debe tener un formato valido")
    private String email;

    @Schema(description = "Nota del cliente", example = "Soy cliente nuevo")
    private String notes;

    @Schema(description = "ID de la BarberShop", example = "db3f9b08-498e-4f0f-a77e-0baa854189eb")
    @NotNull(message = "El cliente debe tener una barbería asignada")
    private BarberShopResponseDto barberShop;
}
