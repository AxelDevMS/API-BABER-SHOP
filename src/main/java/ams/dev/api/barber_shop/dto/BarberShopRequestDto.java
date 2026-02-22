package ams.dev.api.barber_shop.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BarberShopRequestDto implements Serializable {

    @NotNull(message = "El nombre no puede ser nulo")
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotNull(message = "El nombre de la tienda no puede ser nulo")
    @NotBlank(message = "El nombre de la tienda es obligatorio")
    private String commercialName;

    private String taxId;

    @NotNull(message = "La dirección no puede ser nula")
    @NotBlank(message = "La dirección es obligatoria")
    private String address;

    @NotNull(message = "El teléfono no puede ser nulo")
    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(
            regexp = "^\\+?[0-9]{1,3}[\\s.-]?[0-9]{1,4}[\\s.-]?[0-9]{1,4}[\\s.-]?[0-9]{1,9}$",
            message = "El teléfono debe tener un formato válido (ej: +1234567890, 123-456-7890, etc)"
    )
    private String phone;

    @NotNull(message = "El teléfono no puede ser nulo")
    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(
            regexp = "^\\+?[0-9]{1,3}[\\s.-]?[0-9]{1,4}[\\s.-]?[0-9]{1,4}[\\s.-]?[0-9]{1,9}$",
            message = "El teléfono debe tener un formato válido (ej: +1234567890, 123-456-7890, etc)"
    )
    private String mobile;

    @NotNull(message = "El correo electrónico no puede ser nulo")
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico debe tener un formato válido")
    private String email;

    @NotNull(message = "La URL del logo no puede ser nula")
    @NotBlank(message = "La URL del logo es obligatoria")
    private String logoUrl;

    @NotNull(message = "La hora de apertura no puede ser nula")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime openingTime;

    @NotNull(message = "La hora de cierre no puede ser nula")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime closingTime;

    @NotNull(message = "el usuario no puede ser nulo")
    @Valid
    private UserRequestDto user;


}
