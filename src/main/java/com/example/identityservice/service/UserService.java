package com.example.identityservice.service;

import com.example.identityservice.dto.request.UserDto;
import com.example.identityservice.dto.response.UserResponse;
import com.example.identityservice.entity.User;
import com.example.identityservice.enums.Roles;
import com.example.identityservice.exeption.AppException;
import com.example.identityservice.exeption.ErrorCode;
import com.example.identityservice.mapper.UserMapper;
import com.example.identityservice.repository.RoleRepository;
import com.example.identityservice.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            log.info(user.getRoles().toString());

            return List.of(userMapper.toUserResponse(user));

        }
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    public UserResponse userInformation() {
        var context = SecurityContextHolder.getContext();
        String  username = context.getAuthentication().getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userMapper.toUserResponse(user);
    }

    public User createUser(UserDto userDto) {

        boolean userExisted = userRepository.existsByUsername(userDto.getUsername());

        if (userExisted) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        var roles = roleRepository.findAllById(userDto.getRoles());

        user.setRoles(new HashSet<>(roles));

        return userRepository.save(user);
    }

    public UserResponse editUser(UserDto userDto, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userDto.getFirstName() == null) {
            userDto.setFirstName(user.getFirstName());
        }

        if (userDto.getLastName() == null){
            userDto.setLastName(user.getLastName());
        }

        if (userDto.getPassword() == null){
            userDto.setPassword(user.getPassword());
        }

        if (userDto.getDob() == null){
            userDto.setDob(user.getDob());
        }

        userMapper.userUpdate(user, userDto);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
