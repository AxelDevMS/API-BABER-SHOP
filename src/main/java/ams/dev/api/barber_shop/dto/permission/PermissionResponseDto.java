package ams.dev.api.barber_shop.dto.permission;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PermissionResponseDto implements Serializable {

    @Schema(description = "ID del permiso", example = "db3f9b08-498e-4f0f-a77e-0baa854189eb")
    private String id;

    @Schema(description = "Nombre del permiso", example = "VER_LISTA_PERMISO")
    private String name;

    @Schema(description = "Nombre del module", example = "PERMISOS")
    private String module;

    @Schema(description = "Tipo de Acción", example = "READ")
    private String action;

    @Schema(description = "Descripción del permiso", example = "Permiso para ver listado de permisos")
    private String description;

    @Schema(description = "Indicador si el permiso esta activo", example = "true")
    private Boolean isActive;

    @Schema(description = "Indicador si el permiso esta eliminado", example = "true")
    private Boolean isDeleted;

    @Schema(description = "Fecha de creación", example = "2024-01-15T10:30:00")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Mexico_City")
    private Date createdAt;

    @Schema(description = "Fecha de actualización", example = "2024-01-15T10:30:00")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Mexico_City")
    private Date updatedAt;

    public PermissionResponseDto(String id, String name) {
        this.id = id;
        this.name = name;
    }

}
