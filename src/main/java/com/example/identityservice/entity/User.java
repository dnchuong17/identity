package com.example.identityservice.entity;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // generate id for user random and never duplicate
    long id;

    @Column(name="username", unique = true, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String username;
    @Column(name="password", columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String password;
    @Column(name="firstName", columnDefinition = "VARCHAR(255)")
    String firstName;
    @Column(name="lastName", columnDefinition = "VARCHAR(255)")
    String lastName;
    LocalDate dob;

    @ManyToMany(fetch = FetchType.EAGER)
    Set<Role> roles;
}
