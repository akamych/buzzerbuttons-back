package com.akamych.buzzers.controllers;

import com.akamych.buzzers.dtos.responses.AuthResponse;
import com.akamych.buzzers.entities.User;
import com.akamych.buzzers.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService ;

    @GetMapping("/auth")
    public AuthResponse checkAuth(@AuthenticationPrincipal User user) {
        return userService.checkAuth(user);
    }

    @GetMapping("/host")
    public AuthResponse createHost(HttpServletResponse response) {
        return userService.createHost(response);
    }

    @GetMapping("/play")
    public AuthResponse createPlayer(HttpServletResponse response) {
        return userService.createPlayer(response);
    }

    @GetMapping("/log-out")
    public boolean logout(HttpServletResponse response, @AuthenticationPrincipal User user) {
        return userService.logout(response, user);
    }
}