package com.nisum.nisumapi.service;

import com.nisum.nisumapi.config.PasswordConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordService {
    private final PasswordConfig passwordConfig;

    public boolean isValidPassword(String password) {
        if (password == null) {
            return false;
        }
        return password.matches(passwordConfig.getPasswordRegex());
    }
}
