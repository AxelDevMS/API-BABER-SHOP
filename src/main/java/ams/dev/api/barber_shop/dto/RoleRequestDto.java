package ams.dev.api.barber_shop.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleRequestDto implements Serializable {
    private String id;
    private String name;
    private String description;
    private List<PermissionRequestDto> permissions;

}
