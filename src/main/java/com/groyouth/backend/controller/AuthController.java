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
//   token for testing {
//        "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyd0Bncm95b3V0aC5jb20iLCJpYXQiOjE3Njg4MzE2MDMsImV4cCI6MTc2ODkxODAwM30.vAp2YUd8sEmm5XwZacuvCHFkt1vHeMeRhwWKLri50jc",
//            "refreshToken": "d122a3b7-e3e1-41e6-b413-91d0c6d6cb10"
//    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestBody String refreshToken) {
        RefreshToken rt = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
        if (rt.isRevoked() || rt.getExpiresAt().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token expired");
        }
        String newAccessToken = jwtUtil.generateToken(rt.getUser().getEmail(), rt.getUser().getRole().name());
        return new AuthResponse(newAccessToken, refreshToken);
    }

}
