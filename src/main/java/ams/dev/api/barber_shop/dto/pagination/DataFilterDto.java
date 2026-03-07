package ams.dev.api.barber_shop.dto.pagination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataFilterDto implements Serializable {

    private String barbershopId;
    private Boolean active;
    private Boolean deleted;
    private Boolean vip;
    private LocalDateTime createdAfter;
    private LocalDateTime createdBefore;
    private String searchTerm;
    private PageParamRequestDto pageParam;
}
