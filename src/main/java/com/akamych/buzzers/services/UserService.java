package com.akamych.buzzers.services;

import com.akamych.buzzers.dtos.responses.AuthResponse;
import com.akamych.buzzers.entities.Game;
import com.akamych.buzzers.entities.User;
import com.akamych.buzzers.enums.UserRolesEnum;
import com.akamych.buzzers.repositories.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final GameService gameService;
    private final JwtService jwtService;

    public AuthResponse hostGame(HttpServletResponse response) {

        Game newGame = gameService.createGame();

        User user = userRepository.saveAndFlush(User.builder()
                .role(UserRolesEnum.HOST.getRole())
                .game(newGame)
                .build());

        user.setJwtToken(jwtService.generateToken(user));
        userRepository.saveAndFlush(user);

        jwtService.setJwtCookie(user, response);

        return AuthResponse.builder()
                .host(true)
                .game(newGame.getGameId())
                .build();
    }
}
