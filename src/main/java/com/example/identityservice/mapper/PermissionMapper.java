package com.example.identityservice.mapper;

import com.example.identityservice.dto.request.PermissionDto;
import com.example.identityservice.dto.response.PermissionResponse;
import com.example.identityservice.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionDto permissionDto);
    PermissionResponse toPermissionResponse(Permission permission);
}
