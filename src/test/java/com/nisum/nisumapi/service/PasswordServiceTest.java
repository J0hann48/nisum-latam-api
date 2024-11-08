package com.nisum.nisumapi.service;

import com.nisum.nisumapi.config.PasswordConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PasswordServiceTest {
    @Mock
    private PasswordConfig passwordConfig;

    private PasswordService passwordService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        passwordService = new PasswordService(passwordConfig);
        when(passwordConfig.getPasswordRegex()).thenReturn("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$"); // Ejemplo de regex
    }

    @Test
    public void testValidPassword() {
        assertTrue(passwordService.isValidPassword("Password123"), "La contraseña debería ser válida");
    }

    @Test
    public void testInvalidPassword() {
        assertFalse(passwordService.isValidPassword("short"), "La contraseña debería ser inválida");
    }

    @Test
    public void testNullPassword() {
        assertFalse(passwordService.isValidPassword(null), "La contraseña nula debería ser inválida");
    }

}