package ams.dev.api.barber_shop.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serializable;

@Data
public class PermissionRequestDto implements Serializable {

    @NotNull(message = "El nombre no puede ser nulo")
    @NotBlank(message = "El nombre es obligatorio")
    @Pattern(
            regexp = "^[a-zA-Z]+(_[a-zA-Z]+)*$",
            message = "El nombre debe tener el formato: texto_texto_texto (ej: VER_CLIENTES, ELIMINAR_CLIENTE, GESTIONAR_USUARIOS_ROLES, etc)"
    )
    private String name;

    @NotNull(message = "El modulo no puede ser nulo")
    @NotBlank(message = "El modulo es obligatorio")
    private String module;

    @NotNull(message = "El campo acción no puede ser nulo")
    @NotBlank(message = "El campo acción es obligatorio")
    private String action;


    private String description;

}
