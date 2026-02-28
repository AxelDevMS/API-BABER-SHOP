package ams.dev.api.barber_shop.dto.client;

import ams.dev.api.barber_shop.dto.BarberShopResponseDto;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class ClientRequestDto implements Serializable {

    @NotNull(message = "El nombre no puede ser nulo")
    @NotBlank(message = "El nombre es obligatorio")
    private String fullName;

    @NotNull(message = "El teléfono no puede ser nulo")
    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(
            regexp = "^\\+?[0-9]{1,3}[\\s.-]?[0-9]{1,4}[\\s.-]?[0-9]{1,4}[\\s.-]?[0-9]{1,9}$",
            message = "El teléfono debe tener un formato válido (ej: +1234567890, 123-456-7890, etc)"
    )
    private String phone;

    @Email(message = "El correo electrónico debe tener un formato valido")
    private String email;

    private String notes;

    @NotNull(message = "El cliente debe tener una barbería asignada")
    private BarberShopResponseDto barberShop;
}
