package com.akamych.buzzers.services;

import com.akamych.buzzers.dtos.AuthResponse;
import com.akamych.buzzers.dtos.JoinGameRequest;
import com.akamych.buzzers.entities.Game;
import com.akamych.buzzers.entities.User;
import com.akamych.buzzers.enums.UserRolesEnum;
import com.akamych.buzzers.repositories.GameRepository;
import com.akamych.buzzers.repositories.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final GameService gameService;
    private final JwtService jwtService;

    public AuthResponse createHost(HttpServletResponse response) {

        Game newGame = gameService.createGame();

        User user = User.builder()
                .role(UserRolesEnum.HOST.getRole())
                .hostingGame(newGame)
                .build();

        userRepository.saveAndFlush(user);

        jwtService.setJwtCookie(user, response);

        return AuthResponse.builder()
                .host(true)
                .game(newGame.getGameId())
                .build();
    }

    public AuthResponse createPlayer(HttpServletResponse response) {

        User user = User.builder()
                .role(UserRolesEnum.PLAYER.getRole())
                .build();

        userRepository.saveAndFlush(user);
        jwtService.setJwtCookie(user, response);

        return AuthResponse.builder()
                .host(false)
                .build();
    }

    public AuthResponse checkAuth(User user) {

        if (user == null) {
            return null;
        }


        if (user.getRole().equals(UserRolesEnum.HOST.getRole())) {
            return AuthResponse.builder()
                    .host(true)
                    .game(user.getHostingGame().getGameId())
                    .build();
        }

        return AuthResponse.builder()
                .host(false)
                .game(user.getPlayingGame() == null ? null : user.getPlayingGame().getGameId())
                .build();
    }

    @Transactional
    public boolean joinGame(JoinGameRequest request, User user) {

        Game game = gameService.getById(request.game());
        if (game == null) {
            return false;
        }

        user.setPlayingGame(game);
        user.setTeamName(request.name());
        userRepository.saveAndFlush(user);

        game.getPlayers().add(user);
        gameRepository.saveAndFlush(game);

        return true;
    }

    @Transactional
    public boolean logout(HttpServletResponse response, User user) {

        if (user == null) {
            return false;
        }

        SecurityContextHolder.clearContext();
        userRepository.deleteById(user.getId());
        jwtService.deleteJwtCookie(response);

        return true;
    }
}
