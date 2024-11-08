package com.nisum.nisumapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nisum.nisumapi.config.JwtService;
import com.nisum.nisumapi.config.PasswordConfig;
import com.nisum.nisumapi.config.SecurityConfiguration;
import com.nisum.nisumapi.config.annotation.TestSecurityConfig;
import com.nisum.nisumapi.config.filters.JwtAuthorizationFilter;
import com.nisum.nisumapi.controller.user.UserRequest;
import com.nisum.nisumapi.controller.user.UserResponse;
import com.nisum.nisumapi.model.PhoneEntity;
import com.nisum.nisumapi.service.PasswordService;
import com.nisum.nisumapi.service.UserDetailsServiceImpl;
import com.nisum.nisumapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService; // Mock del servicio UserService

    @MockBean
    private PasswordService passwordService; // Mock del servicio PasswordService

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserRequest validUserRequest;
    private UserResponse validUserResponse;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        validUserRequest = new UserRequest();
        validUserRequest.setName("Test User");
        validUserRequest.setEmail("test@example.com");
        validUserRequest.setPassword("ValidPassword123!");

        validUserResponse = new UserResponse();
        validUserResponse.setId(UUID.randomUUID());
        validUserResponse.setName("Test User");
        validUserResponse.setEmail("test@example.com");

        when(passwordService.isValidPassword(any(String.class))).thenReturn(true);

    }

    @Test
    @WithMockUser
    public void testCreateUser_Success() throws Exception {
        // Mock para el método de validación de contraseña
        when(passwordService.isValidPassword(any(String.class))).thenReturn(true);
        when(userService.createUser(any(UserRequest.class))).thenReturn(validUserResponse);

        mockMvc.perform(post("/v1/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUserRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }
}