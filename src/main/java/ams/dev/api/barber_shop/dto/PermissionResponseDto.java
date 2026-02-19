package ams.dev.api.barber_shop.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Mexico_City")
    private Date createdAt;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Mexico_City")
    private Date updatedAt;
}
