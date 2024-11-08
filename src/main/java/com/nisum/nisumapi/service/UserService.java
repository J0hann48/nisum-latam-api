package com.nisum.nisumapi.service;

import com.nisum.nisumapi.config.JwtService;
import com.nisum.nisumapi.controller.user.UserRequest;
import com.nisum.nisumapi.controller.user.UserResponse;
import com.nisum.nisumapi.mapper.GenericMapper;
import com.nisum.nisumapi.model.UserEntity;
import com.nisum.nisumapi.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mapping.MappingException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final PasswordService passwordService;

    @Qualifier("userRequestToEntityMapper")
    private final GenericMapper<UserRequest, UserEntity> requestUserEntityGenericMapper;

    @Qualifier("userResponseToEntityMapper")
    private final GenericMapper<UserResponse, UserEntity> responseUserEntityGenericMapper;


    @Transactional
    public UserResponse createUser(UserRequest userRequest) {
        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()){
            throw new IllegalStateException("El correo ya registrado");
        }
        if (!passwordService.isValidPassword(userRequest.getPassword())) {
            throw new IllegalArgumentException("La contraseña no cumple con el formato requerido");
        }

        try{
            UserEntity userEntity = requestUserEntityGenericMapper.toEntity(userRequest);
            String token = jwtService.generateAccessToken(userEntity.getEmail());
            userEntity.setToken(token);
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            userEntity.setActive(true);
            userEntity.setCreated(new Date(System.currentTimeMillis()));
            userEntity.setModified(new Date(System.currentTimeMillis()));
            userEntity.setLastLogin(new Date(System.currentTimeMillis()));
            userEntity = userRepository.save(userEntity);
            return responseUserEntityGenericMapper.toDTO(userEntity);
        } catch (MappingException e) {
            throw new IllegalStateException("Error al mapear el UserRequest a UserEntity", e);
        } catch (Exception e) {
            throw new RuntimeException("Ocurrió un error al crear el usuario", e);
        }
    }

    @Transactional
    public UserResponse updateUser(UserRequest userRequest){
        if (userRequest.getEmail() == null || userRequest.getEmail().isBlank()) {
            throw new IllegalArgumentException("El correo no debe estar vacío");
        }
        UserEntity user = userRepository.findByEmail(userRequest.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("El usuario no existe"));

        user.setName(userRequest.getName() != null ? userRequest.getName() : user.getName());
        user.setPhones(userRequest.getPhones() != null ? userRequest.getPhones() : user.getPhones());
        user.setLastLogin(new Date(System.currentTimeMillis()));
        user.setModified(new Date(System.currentTimeMillis()));
        user = userRepository.save(user);
        return responseUserEntityGenericMapper.toDTO(user);
    }
}
