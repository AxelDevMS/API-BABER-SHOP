package ams.dev.api.barber_shop.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDto implements Serializable {
    private String id;
    private String name;
    private String lastName;
    private String phone;
    private String username;
    private String email;
    private Boolean isActive;
    private Boolean isDeleted;
    private Date lastLogin;
    private RoleResponseDto role;

    public UserResponseDto(String id, String name, String lastName, String phone, String username) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.phone = phone;
        this.username = username;
    }
}
