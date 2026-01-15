package com.groyouth.backend.service;

import com.groyouth.backend.dto.AuthResponse;
import com.groyouth.backend.dto.LoginRequest;
import com.groyouth.backend.dto.SignupRequest;
import com.groyouth.backend.model.RefreshToken;
import com.groyouth.backend.model.Role;
import com.groyouth.backend.model.User;
import com.groyouth.backend.repository.RefreshTokenRepository;
import com.groyouth.backend.repository.UserRepository;
import com.groyouth.backend.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil,
                       RefreshTokenRepository refreshTokenRepository){
        this.userRepository= userRepository;
        this.passwordEncoder=passwordEncoder;
        this.jwtUtil= jwtUtil;
        this.refreshTokenRepository= refreshTokenRepository;
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

    public AuthResponse login(LoginRequest request){
        User user = userRepository.findByEmail(request.email)
                .orElseThrow(()-> new RuntimeException("User not found"));

        if(!passwordEncoder.matches(request.password, user.getPassword())){
            throw new RuntimeException("Invalid Password");
        }

        String accessToken = jwtUtil.generateToken(user.getEmail());

        String refreshToken = UUID.randomUUID().toString();

        RefreshToken rt = new RefreshToken();
        rt.setToken(refreshToken);
        rt.setUser(user);
        rt.setExpiresAt(Instant.now().plus(30, ChronoUnit.DAYS));

        refreshTokenRepository.save(rt);

        return new AuthResponse(accessToken, refreshToken);

    }
}
