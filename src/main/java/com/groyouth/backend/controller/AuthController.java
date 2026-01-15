package com.groyouth.backend.controller;

import com.groyouth.backend.dto.AuthResponse;
import com.groyouth.backend.dto.LoginRequest;
import com.groyouth.backend.dto.SignupRequest;
import com.groyouth.backend.model.RefreshToken;
import com.groyouth.backend.model.User;
import com.groyouth.backend.repository.RefreshTokenRepository;
import com.groyouth.backend.security.JwtUtil;
import com.groyouth.backend.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, RefreshTokenRepository refreshTokenRepository,
                          JwtUtil jwtUtil){
        this.userService=userService;
        this.refreshTokenRepository= refreshTokenRepository;
        this.jwtUtil=jwtUtil;
    }

    @PostMapping("/signup")
    public User signup(@RequestBody SignupRequest request){
        return userService.signup(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request){
        return userService.login(request);
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestBody String refreshToken) {

        RefreshToken rt = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (rt.isRevoked() || rt.getExpiresAt().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token expired");
        }

        String newAccessToken = jwtUtil.generateToken(rt.getUser().getEmail());

        return new AuthResponse(newAccessToken, refreshToken);
    }

}
