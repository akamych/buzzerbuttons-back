package com.akamych.buzzers.controllers;

import com.akamych.buzzers.dtos.AuthResponse;
import com.akamych.buzzers.dtos.GameInfoResponse;
import com.akamych.buzzers.dtos.JoinGameRequest;
import com.akamych.buzzers.entities.User;
import com.akamych.buzzers.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class UserController {
    private final UserService userService ;

    @GetMapping("/auth")
    public AuthResponse checkAuth(@AuthenticationPrincipal User user) {
        return userService.checkAuth(user);
    }

    @GetMapping("/host")
    public AuthResponse createHost(HttpServletResponse response, @AuthenticationPrincipal User user) {
        return userService.createHost(response, user);
    }

    @GetMapping("/play")
    public AuthResponse createPlayer(HttpServletResponse response, @AuthenticationPrincipal User user) {
        return userService.createPlayer(response, user);
    }

    @PostMapping("/join")
    public boolean joinGame(@RequestBody JoinGameRequest request, @AuthenticationPrincipal User user) {
        return userService.joinGame(request, user);
    }

    @GetMapping("/get-name")
    public String getPlayerName(@AuthenticationPrincipal User user) {
        return user == null ? null : user.getPlayerName();
    }

    @GetMapping("/log-out")
    public boolean logout(HttpServletResponse response, @AuthenticationPrincipal User user) {
        return userService.logout(response, user);
    }

    @GetMapping("/game-info")
    public GameInfoResponse getGameInfo(@AuthenticationPrincipal User user) {
        return userService.getGameInfo(user);
    }
}