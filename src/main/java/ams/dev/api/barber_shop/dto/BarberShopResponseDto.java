package ams.dev.api.barber_shop.dto;

import ams.dev.api.barber_shop.enums.BarberShopStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BarberShopResponseDto implements Serializable {
    private String id;
    private String name;
    private String commercialName;
    private String taxId;
    private String address;
    private String phone;
    private String mobile;
    private String email;
    private String logoUrl;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime openingTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime closingTime;
    private String timezone;
    private BarberShopStatus status;
    private List<UserResponseDto> users;
}
