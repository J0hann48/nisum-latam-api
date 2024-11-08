package com.nisum.nisumapi.controller.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nisum.nisumapi.model.PhoneEntity;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Set;

@Data
public class UserRequest {
    @JsonProperty("name")
    private String name;
    @JsonProperty("email")
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "El correo debe tener un formato válido"
    )
    private String email;
    @JsonProperty("password")
    private String password;
    @JsonProperty("phones")
    private Set<PhoneEntity> phones;

}
