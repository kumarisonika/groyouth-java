package com.groyouth.backend.service;

import com.groyouth.backend.dto.LoginRequest;
import com.groyouth.backend.dto.SignupRequest;
import com.groyouth.backend.model.Role;
import com.groyouth.backend.model.User;
import com.groyouth.backend.repository.UserRepository;
import com.groyouth.backend.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil){
        this.userRepository= userRepository;
        this.passwordEncoder=passwordEncoder;
        this.jwtUtil= jwtUtil;
    }

    public User signup(SignupRequest request) {
        User user = new User();
        user.setName(request.name);
        user.setEmail(request.email);

        user.setPassword(passwordEncoder.encode(request.password));

//        user.setPassword(request.password);
        user.setRole(Role.valueOf(request.role));
        return userRepository.save(user);
    }

    public String login(LoginRequest request){
        User user = userRepository.findByEmail(request.email)
                .orElseThrow(()-> new RuntimeException("User not found"));

        if(!passwordEncoder.matches(request.password, user.getPassword())){
            throw new RuntimeException("Invalid Password");
        }

        return jwtUtil.generateToken(user.getEmail());
    }
}
