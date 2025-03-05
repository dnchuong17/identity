package com.example.identityservice.controller;

import com.example.identityservice.dto.request.ApiResponse;
import com.example.identityservice.dto.request.UserDto;
import com.example.identityservice.dto.response.UserResponse;
import com.example.identityservice.entity.User;
import com.example.identityservice.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    ApiResponse<User> createUser(@RequestBody @Valid UserDto userDto) {
        ApiResponse<User> apiResponse = new ApiResponse<>();
        User user = userService.createUser(userDto);
        apiResponse.setResult(user);
        return  apiResponse;
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
