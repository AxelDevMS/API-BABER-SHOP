package ams.dev.api.barber_shop.dto.pagination;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageParamRequestDto implements Serializable {
    private int page;
    private int size;
    private String sortBy;
    private String sortDirection;
}
