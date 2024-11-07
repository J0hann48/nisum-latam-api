package com.nisum.nisumapi.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class PasswordConfig {
    @Value("${password.regex}")
    private String passwordRegex;
}
