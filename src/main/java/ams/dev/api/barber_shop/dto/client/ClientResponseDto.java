package ams.dev.api.barber_shop.dto.client;

import ams.dev.api.barber_shop.dto.BarberShopResponseDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class ClientResponseDto implements Serializable {

    private String id;
    private String fullName;
    private String phone;
    private String email;
    private Boolean isVip;
    private Boolean isActive;
    private Boolean isDeleted;
    private String notes;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Mexico_City")
    private Date createdAt;
    private BarberShopResponseDto barberShop;
}
