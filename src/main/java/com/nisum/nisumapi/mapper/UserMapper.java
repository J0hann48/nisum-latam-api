package com.nisum.nisumapi.mapper;

import com.nisum.nisumapi.controller.user.UserRequest;
import com.nisum.nisumapi.controller.user.UserResponse;
import com.nisum.nisumapi.model.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(UserEntity userEntity);
    UserEntity toUserEntity(UserRequest userRequest);
}
