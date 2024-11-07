package com.nisum.nisumapi.service;

import com.nisum.nisumapi.config.JwtService;
import com.nisum.nisumapi.controller.user.UserRequest;
import com.nisum.nisumapi.controller.user.UserResponse;
import com.nisum.nisumapi.mapper.GenericMapper;
import com.nisum.nisumapi.mapper.UserMapper;
import com.nisum.nisumapi.model.UserEntity;
import com.nisum.nisumapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    @Qualifier("userRequestToEntityMapper")
    private final GenericMapper<UserRequest, UserEntity> requestUserEntityGenericMapper;
    @Qualifier("userResponseToEntityMapper")
    private final GenericMapper<UserResponse, UserEntity> responseUserEntityGenericMapper;


    @Transactional
    public UserResponse createUser(UserRequest userRequest){
        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()){
            throw new IllegalStateException("El correo ya registrado");
        }

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
    }

    @Transactional
    public UserResponse updateUser(UserRequest userRequest){
        if (userRepository.findByEmail(userRequest.getEmail()).isEmpty()){
            throw new IllegalStateException("El usuario no existe");
        }
        //TODO validar token para actualizar
        throw new NotImplementedException();
    }
}
