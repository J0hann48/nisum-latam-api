package com.nisum.nisumapi.controller.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nisum.nisumapi.model.PhoneEntity;
import lombok.Data;

import java.sql.Date;
import java.util.Set;
import java.util.UUID;

@Data
public class UserResponse {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("email")
    private String email;
    @JsonProperty("created")
    private Date created;
    @JsonProperty("modified")
    private Date modified;
    @JsonProperty("last_login")
    private Date lastLogin;
    @JsonProperty("token")
    private String token;
    @JsonProperty("is_active")
    private boolean isActive;
    @JsonProperty("phones")
    private Set<PhoneEntity> phones;
}
