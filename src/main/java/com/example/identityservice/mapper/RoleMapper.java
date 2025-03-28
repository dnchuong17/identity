package com.example.identityservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.identityservice.dto.request.RoleDto;
import com.example.identityservice.dto.response.RoleResponse;
import com.example.identityservice.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleDto roleDto);

    RoleResponse toRoleResponse(Role role);
}
