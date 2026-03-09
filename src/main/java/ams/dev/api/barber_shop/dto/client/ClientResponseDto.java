package ams.dev.api.barber_shop.dto.client;

import ams.dev.api.barber_shop.dto.BarberShopResponseDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Respuesta con datos del cliente")
public class ClientResponseDto implements Serializable {

    @Schema(description = "ID del cliente", example = "db3f9b08-498e-4f0f-a77e-0baa854189eb")
    private String id;

    @Schema(description = "Nombre del cliente", example = "Juan Pérez")
    private String fullName;


    private String phone;

    @Schema(description = "Email del cliente", example = "juan@email.com")
    private String email;

    @Schema(description = "Indicador si es VIP", example = "true")
    private Boolean isVip;

    @Schema(description = "Indicador si el cliente esta activo", example = "true")
    private Boolean isActive;

    @Schema(description = "Indicador si el cliente esta eliminado", example = "true")
    private Boolean isDeleted;

    @Schema(description = "Nota del cliente", example = "Soy cliente nuevo")
    private String notes;

    @Schema(description = "Fecha de creación", example = "2024-01-15T10:30:00")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Mexico_City")
    private Date createdAt;

    @Schema(description = "ID de la BarberShop", example = "db3f9b08-498e-4f0f-a77e-0baa854189eb")
    private BarberShopResponseDto barberShop;
}
