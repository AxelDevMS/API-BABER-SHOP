package ams.dev.api.barber_shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailDataDto implements Serializable {
    private String toEamil;
    private String name;
    private String username;
    private String password;
}
