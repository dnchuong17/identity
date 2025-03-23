package com.example.identityservice.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.identityservice.constant.PredefinedRole;
import com.example.identityservice.dto.request.UserDto;
import com.example.identityservice.dto.response.UserResponse;
import com.example.identityservice.entity.Role;
import com.example.identityservice.entity.User;
import com.example.identityservice.exeption.AppException;
import com.example.identityservice.exeption.ErrorCode;
import com.example.identityservice.mapper.UserMapper;
import com.example.identityservice.repository.RoleRepository;
import com.example.identityservice.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers(Long id) {
        if (id != null) {
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
            log.info(user.getRoles().toString());

            return List.of(userMapper.toUserResponse(user));
        }
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    public UserResponse userInformation() {
        var context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        return userMapper.toUserResponse(user);
    }

    public UserResponse createUser(UserDto userDto) {

        User user = userMapper.toUser(userDto);

        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        HashSet<Role> roles = new HashSet<>();

        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);

        user.setRoles(new HashSet<>(roles));

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        return userMapper.toUserResponse(user);
    }

    public UserResponse editUser(UserDto userDto, Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        if (userDto.getUsername() == null) {
            userDto.setUsername(user.getUsername());
        }

        if (userDto.getFirstName() == null) {
            userDto.setFirstName(user.getFirstName());
        }

        if (userDto.getLastName() == null) {
            userDto.setLastName(user.getLastName());
        }

        if (userDto.getPassword() == null) {
            userDto.setPassword(user.getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        if (userDto.getDob() == null) {
            userDto.setDob(user.getDob());
        }

        userMapper.userUpdate(user, userDto);

        var roles = roleRepository.findAllById(userDto.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
