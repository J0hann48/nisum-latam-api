package com.nisum.nisumapi.mapper.user;

import com.nisum.nisumapi.controller.user.UserRequest;
import com.nisum.nisumapi.mapper.GenericMapper;
import com.nisum.nisumapi.model.UserEntity;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component("userRequestToEntityMapper")
public class UserRequestMapperImpl implements GenericMapper<UserRequest, UserEntity> {
    @Override
    public UserEntity toEntity(UserRequest dto) {
        if (dto == null) {
            return null;
        }

        return UserEntity.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .phones(dto.getPhones())
                .build();
    }

    @Override
    public UserRequest toDTO(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        UserRequest dto = new UserRequest();
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setPhones(entity.getPhones());
        return dto;
    }
}
