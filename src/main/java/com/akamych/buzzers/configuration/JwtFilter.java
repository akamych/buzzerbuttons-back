package com.akamych.buzzers.configuration;

import com.akamych.buzzers.repositories.UserRepository;
import com.akamych.buzzers.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    @Value("${BUZZERS_COOKIE_NAME}")
    private String JWT_TOKEN_COOKIE_NAME;

    public JwtFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        if (token == null || !jwtService.validateToken(token)) {
            response.setHeader("Authorization", null);
            chain.doFilter(request, response);
            return;
        }

        String userId = jwtService.getUserId(token);
        if (userId == null) {
            response.setHeader("Authorization", null);
            chain.doFilter(request, response);
            return;
        }

        userRepository.findById(UUID.fromString(userId)).ifPresentOrElse(user -> {
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user, null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            response.setHeader("Authorization", "Bearer " + jwtService.generateToken(user));
        }, () -> {
            response.setHeader("Authorization", null);
        });


        chain.doFilter(request, response);
    }
}