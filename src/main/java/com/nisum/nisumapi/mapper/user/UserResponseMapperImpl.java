package com.nisum.nisumapi.mapper.user;

import com.nisum.nisumapi.controller.user.UserResponse;
import com.nisum.nisumapi.mapper.GenericMapper;
import com.nisum.nisumapi.model.UserEntity;
import org.springframework.stereotype.Component;

@Component("userResponseToEntityMapper")
public class UserResponseMapperImpl implements GenericMapper<UserResponse, UserEntity> {
    @Override
    public UserEntity toEntity(UserResponse dto) {
        if (dto == null) {
            return null;
        }

        return UserEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .token(dto.getToken())
                .phones(dto.getPhones())
                .created(dto.getCreated())
                .modified(dto.getModified())
                .isActive(dto.isActive())
                .lastLogin(dto.getLastLogin())
                .build();
    }

    @Override
    public UserResponse toDTO(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        UserResponse userResponse = new UserResponse();

        userResponse.setId(entity.getId());
        userResponse.setName(entity.getName());
        userResponse.setEmail(entity.getEmail());
        userResponse.setCreated(entity.getCreated());
        userResponse.setModified(entity.getModified());
        userResponse.setLastLogin(entity.getLastLogin());
        userResponse.setToken(entity.getToken());
        userResponse.setActive(entity.isActive());
        userResponse.setPhones(entity.getPhones());
        return userResponse;
    }
}
