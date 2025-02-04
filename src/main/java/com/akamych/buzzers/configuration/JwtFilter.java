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

        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            chain.doFilter(request, response);
            return;
        }

        Optional<Cookie> jwtTokenCookie = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equalsIgnoreCase(JWT_TOKEN_COOKIE_NAME))
                .findFirst();

        if (jwtTokenCookie.isEmpty()) {
            chain.doFilter(request, response);
            return;
        }

        String token = jwtTokenCookie.get().getValue();

        if (token != null && jwtService.validateToken(token)) {
            String userId = jwtService.getUserId(token);
            userRepository.findById(UUID.fromString(userId)).ifPresent(user -> {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user, null, null);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            });
        }
        chain.doFilter(request, response);
    }
}