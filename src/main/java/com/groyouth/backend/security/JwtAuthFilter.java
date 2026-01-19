package com.groyouth.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException{
        //read auth header
        String authHeader = request.getHeader("Authorization");

        //if no token, continue(security config will decide)
        if(authHeader==null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }

        //Extract jwt
        String token = authHeader.substring(7);

        //validate JWT
        if(!jwtUtil.validateToken(token)){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        //extract user identity
        String email = jwtUtil.extractEmail(token);

        //create authentication object
        String role = jwtUtil.extractRole(token);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        List.of(() -> "ROLE_" + role)
                );


        //Set authentication in security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //continue request
        filterChain.doFilter(request,response);

    }
}
