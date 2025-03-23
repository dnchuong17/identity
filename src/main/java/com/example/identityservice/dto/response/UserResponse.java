package com.example.identityservice.dto.response;

import java.time.LocalDate;
import java.util.Set;

import com.example.identityservice.entity.Role;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    long id;
    String username;
    String firstName;
    String lastName;
    LocalDate dob;

    Set<Role> roles;
}
