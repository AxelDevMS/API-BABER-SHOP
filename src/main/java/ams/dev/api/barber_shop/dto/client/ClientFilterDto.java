package ams.dev.api.barber_shop.dto.client;

import ams.dev.api.barber_shop.dto.pagination.DataFilterDto;
import ams.dev.api.barber_shop.dto.pagination.PageParamRequestDto;
import lombok.*;

import java.time.LocalDateTime;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientFilterDto extends DataFilterDto {
    private String clientId;
    private Boolean vip;
    private Boolean active;
    private Boolean deleted;

    public ClientFilterDto(String barbershopId, LocalDateTime createdAfter, LocalDateTime createdBefore, String searchTerm, PageParamRequestDto pageParam,
                           String clientId, Boolean vip, Boolean active, Boolean deleted)
    {
        super(barbershopId, createdAfter, createdBefore, searchTerm, pageParam);
        this.clientId = clientId;
        this.vip = vip;
        this.active = active;
        this.deleted = deleted;
    }


}
