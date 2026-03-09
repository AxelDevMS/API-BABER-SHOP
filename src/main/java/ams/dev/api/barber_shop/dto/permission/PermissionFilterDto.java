package ams.dev.api.barber_shop.dto.permission;

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
public class PermissionFilterDto extends DataFilterDto {
    private String permissionId;
    private String module;
    private Boolean deleted;
    private Boolean active;


    public PermissionFilterDto(String barbershopId, LocalDateTime createdAfter, LocalDateTime createdBefore,
                               String searchTerm, PageParamRequestDto pageParam, String permissionId,
                               String module, Boolean deleted, Boolean active)
    {
        super(barbershopId, createdAfter, createdBefore, searchTerm, pageParam);
        this.permissionId = permissionId;
        this.module = module;
        this.deleted = deleted;
        this.active = active;
    }

}
