package ams.dev.api.barber_shop.dto.service;


import ams.dev.api.barber_shop.dto.pagination.DataFilterDto;
import ams.dev.api.barber_shop.dto.pagination.PageParamRequestDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceFilterDto extends DataFilterDto {
    private String serviceId;
    private Boolean isActive;
    private Boolean isDeleted;

    public ServiceFilterDto(String barbershopId, LocalDateTime createdAfter, LocalDateTime createdBefore, String searchTerm,
                            PageParamRequestDto pageParam, String serviceId, Boolean isActive, Boolean isDeleted)
    {
        super(barbershopId, createdAfter, createdBefore, searchTerm, pageParam);
        this.serviceId = serviceId;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
    }

}
