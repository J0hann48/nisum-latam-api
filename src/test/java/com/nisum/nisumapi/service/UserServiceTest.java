package com.nisum.nisumapi.service;

import com.nisum.nisumapi.config.JwtService;
import com.nisum.nisumapi.controller.user.UserRequest;
import com.nisum.nisumapi.controller.user.UserResponse;
import com.nisum.nisumapi.mapper.GenericMapper;
import com.nisum.nisumapi.model.PhoneEntity;
import com.nisum.nisumapi.model.UserEntity;
import com.nisum.nisumapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.sql.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private PasswordService passwordService;
    @Mock
    private GenericMapper<UserRequest, UserEntity> requestUserEntityGenericMapper;

    @Mock
    private GenericMapper<UserResponse, UserEntity> responseUserEntityGenericMapper;

    @InjectMocks
    private UserService userService;

    private UserRequest userRequest;
    private UserEntity userEntity;
    private UserResponse userResponse;
    private Set<PhoneEntity> phones;

    @BeforeEach
    void setUp() {

        userService = new UserService(userRepository, jwtService, passwordEncoder, passwordService,
                requestUserEntityGenericMapper, responseUserEntityGenericMapper);

        // Configuración de UserRequest
        userRequest = new UserRequest();
        userRequest.setEmail("test@example.com");
        userRequest.setPassword("validPassword");
        userRequest.setName("Test User");

        userEntity = new UserEntity();
        userEntity.setEmail(userRequest.getEmail());
        userEntity.setPassword("encodedPassword");

        userResponse = new UserResponse();
        userResponse.setEmail(userRequest.getEmail());
    }

    @Test
    void createUser_WhenUserDoesNotExistAndPasswordIsValid_ReturnsUserResponse() throws Exception {
        // Arrange
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordService.isValidPassword(userRequest.getPassword())).thenReturn(true);
        when(requestUserEntityGenericMapper.toEntity(userRequest)).thenReturn(userEntity);
        when(jwtService.generateAccessToken(userEntity.getEmail())).thenReturn("token");

        // Simulamos la codificación de la contraseña
        when(passwordEncoder.encode(userRequest.getPassword())).thenReturn("encodedPassword");

        // Asigna la contraseña no codificada antes de la codificación
        userEntity.setPassword(userRequest.getPassword());

        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(responseUserEntityGenericMapper.toDTO(any(UserEntity.class))).thenReturn(userResponse);


        // Act
        UserResponse result = userService.createUser(userRequest);

        // Assert
        assertEquals(userRequest.getEmail(), result.getEmail());
        verify(userRepository).save(userEntity);
    }

    @Test
    void createUser_WhenUserAlreadyExists_ThrowsIllegalStateException() {
        // Arrange
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.of(userEntity));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> userService.createUser(userRequest));
    }

    @Test
    void createUser_WhenPasswordIsInvalid_ThrowsIllegalArgumentException() {
        // Arrange
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordService.isValidPassword(userRequest.getPassword())).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(userRequest));
    }

    @Test
    void updateUser_WhenUserExists_ReturnsUpdatedUserResponse() {
        // Arrange
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.of(userEntity));
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(responseUserEntityGenericMapper.toDTO(userEntity)).thenReturn(userResponse);

        // Act
        UserResponse result = userService.updateUser(userRequest);

        // Assert
        assertEquals(userRequest.getEmail(), result.getEmail());
        verify(userRepository).save(userEntity);
    }

    @Test
    void updateUser_WhenUserDoesNotExist_ThrowsEntityNotFoundException() {
        // Arrange
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(userRequest));
    }

    @Test
    void updateUser_WhenEmailIsBlank_ThrowsIllegalArgumentException() {
        // Arrange
        userRequest.setEmail("");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(userRequest));
    }

    @Test
    void testMapperMock() {
        // Arrange
        when(responseUserEntityGenericMapper.toDTO(any(UserEntity.class))).thenReturn(userResponse);

        // Act
        UserResponse result = responseUserEntityGenericMapper.toDTO(new UserEntity());

        // Assert
        assertNotNull(result);
        assertEquals(userResponse, result);
    }
}