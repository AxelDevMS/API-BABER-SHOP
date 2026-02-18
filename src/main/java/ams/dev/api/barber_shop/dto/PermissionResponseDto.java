package ams.dev.api.barber_shop.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PermissionResponseDto implements Serializable {
    private String id;
    private String name;
    private String module;
    private String action;
    private String description;
    private Date createdAt;
    private Date updatedAt;
}
