package com.example.identityservice.service;

import com.example.identityservice.dto.request.UserCreationRequest;
import com.example.identityservice.entity.User;
import com.example.identityservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createRequest(UserCreationRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDoB(request.getDoB());

        return userRepository.save(user);
    }
}
