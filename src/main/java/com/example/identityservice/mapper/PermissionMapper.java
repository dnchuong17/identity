package com.example.identityservice.mapper;

import org.mapstruct.Mapper;

import com.example.identityservice.dto.request.PermissionDto;
import com.example.identityservice.dto.response.PermissionResponse;
import com.example.identityservice.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionDto permissionDto);

    PermissionResponse toPermissionResponse(Permission permission);
}
