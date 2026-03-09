package ams.dev.api.barber_shop.dto.role;

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
public class RoleFilerDto extends DataFilterDto {
    private String roleId;
    private Boolean isActive;
    private Boolean isDeleted;

    public RoleFilerDto(String barbershopId, LocalDateTime createdAfter, LocalDateTime createdBefore, String searchTerm, PageParamRequestDto pageParam,
                        String roleId, Boolean isActive, Boolean isDeleted)
    {
        super(barbershopId, createdAfter, createdBefore, searchTerm, pageParam);
        this.roleId = roleId;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
    }
}
