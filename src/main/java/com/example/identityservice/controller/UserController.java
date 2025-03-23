package com.example.identityservice.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.example.identityservice.dto.request.ApiResponse;
import com.example.identityservice.dto.request.UserDto;
import com.example.identityservice.dto.response.UserResponse;
import com.example.identityservice.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserService userService;

    @GetMapping
    ApiResponse<List<UserResponse>> getUsers(@RequestParam(required = false) Long id) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers(id))
                .build();
    }

    @GetMapping("/personal")
    UserResponse userInformation() {
        return userService.userInformation();
    }

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserDto userDto) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(userDto))
                .build();
    }

    @PutMapping("/{userId}")
    UserResponse updateUser(@RequestBody UserDto userDto, @PathVariable Long userId) {
        return userService.editUser(userDto, userId);
    }

    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return "Deleted user with id: " + userId;
    }
}
