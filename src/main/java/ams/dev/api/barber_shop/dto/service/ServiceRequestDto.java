package ams.dev.api.barber_shop.dto.service;

import ams.dev.api.barber_shop.dto.BarberShopResponseDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceRequestDto {
    private String serviceId;
    private String name;
    private LocalTime duration;
    private BigDecimal price;
    private Boolean isActive;
    private BarberShopResponseDto barbershop;
}
