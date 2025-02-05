package com.akamych.buzzers.services;

import com.akamych.buzzers.dtos.responses.AuthResponse;
import com.akamych.buzzers.entities.Game;
import com.akamych.buzzers.entities.User;
import com.akamych.buzzers.enums.UserRolesEnum;
import com.akamych.buzzers.repositories.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final GameService gameService;
    private final JwtService jwtService;

    public AuthResponse hostGame(HttpServletResponse response) {

        Game newGame = gameService.createGame();

        User user = User.builder()
                .role(UserRolesEnum.HOST.getRole())
                .game(newGame)
                .build();

        userRepository.saveAndFlush(user);

        jwtService.setJwtCookie(user, response);

        return AuthResponse.builder()
                .host(true)
                .game(newGame.getGameId())
                .build();
    }

    public AuthResponse checkAuth(User user) {

        if (user == null) {
            return null;
        }

        return AuthResponse.builder()
                .host(user.getRole().equals(UserRolesEnum.HOST.getRole()))
                .game(user.getGame().getGameId())
                .build();
    }
}
