package com.groyouth.backend.service;

import com.groyouth.backend.dto.SignupRequest;
import com.groyouth.backend.model.Role;
import com.groyouth.backend.model.User;
import com.groyouth.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository= userRepository;
    }

    public User signup(SignupRequest request) {
        User user = new User();
        user.setName(request.name);
        user.setEmail(request.email);
        user.setPassword(request.password);
        user.setRole(Role.valueOf(request.role));
        return userRepository.save(user);
    }
}
