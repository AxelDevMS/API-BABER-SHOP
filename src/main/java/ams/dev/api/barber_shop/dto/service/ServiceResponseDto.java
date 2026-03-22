package ams.dev.api.barber_shop.dto.service;

import ams.dev.api.barber_shop.dto.BarberShopResponseDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ServiceResponseDto {

    private String serviceId;
    private String name;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime duration;
    private BigDecimal price;
    private Boolean isActive;
    private Boolean isDeleted;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Mexico_City")
    private Date createdAt;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Mexico_City")
    private Date updatedAt;
    private BarberShopResponseDto barbershop;

}
