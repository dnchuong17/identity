package com.example.identityservice.mapper;

import com.example.identityservice.dto.request.UserDto;
import com.example.identityservice.dto.response.UserResponse;
import com.example.identityservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    User toUser(UserDto userDto);
    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void userUpdate(@MappingTarget User user, UserDto userDto);
}
