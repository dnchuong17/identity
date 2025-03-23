package com.example.identityservice.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import com.example.identityservice.dto.request.UserDto;
import com.example.identityservice.dto.response.UserResponse;
import com.example.identityservice.entity.User;
import com.example.identityservice.exeption.AppException;
import com.example.identityservice.repository.RoleRepository;
import com.example.identityservice.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    private UserDto userDto;
    private UserResponse userResponse;
    private User user;
    private LocalDate dob;

    @BeforeEach
    void initData() {
        dob = LocalDate.of(2004, 12, 17);

        userDto = UserDto.builder()
                .username("johndoe1234567")
                .firstName("John")
                .lastName("Doe")
                .password("12345678")
                .dob(dob)
                .build();

        userResponse = UserResponse.builder()
                .id(999)
                .username("johndoe1234567")
                .firstName("John")
                .lastName("Doe")
                .dob(dob)
                .build();

        user = User.builder()
                .id(999)
                .username("johndoe1234567")
                .firstName("John")
                .lastName("Doe")
                .dob(dob)
                .build();
    }

    @Test
    void createUser_validRequest_success() {
        when(roleRepository.findById(anyString())).thenReturn(Optional.ofNullable(null));

        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        var response = userService.createUser(userDto);

        assertThat(response.getId()).isEqualTo(999);
        assertThat(response.getUsername()).isEqualTo("johndoe1234567");
    }

    @Test
    void createUser_userExisted_fail() {
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        var exception = assertThrows(AppException.class, () -> userService.createUser(userDto));

        assertThat(exception.getErrorCode().getCode()).isEqualTo(1000);
    }

    @Test
    void createUser_dataIntegrityViolationException_fail() {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);

        when(userRepository.save(any(User.class)))
                .thenThrow(new DataIntegrityViolationException("Username already exists"));

        var exception = assertThrows(AppException.class, () -> userService.createUser(userDto));

        assertThat(exception.getErrorCode().getCode()).isEqualTo(1000);
    }

    @Test
    @WithMockUser(username = "johndoe1234567")
    void getMyInfo_valid_success() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        var response = userService.userInformation();

        assertThat(response.getUsername()).isEqualTo("johndoe1234567");
        assertThat(response.getFirstName()).isEqualTo("John");
    }

    @Test
    @WithMockUser(username = "johndoe1234567")
    void getMyInfo_inValid_fail() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.ofNullable(null));

        var exception = assertThrows(RuntimeException.class, () -> userService.userInformation());

        assertThat(exception.getMessage()).isEqualTo("User not found");
    }
}
