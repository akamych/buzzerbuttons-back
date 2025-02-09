package com.akamych.buzzers.services;

import com.akamych.buzzers.dtos.AuthResponse;
import com.akamych.buzzers.dtos.GameInfoResponse;
import com.akamych.buzzers.dtos.JoinGameRequest;
import com.akamych.buzzers.entities.Game;
import com.akamych.buzzers.entities.User;
import com.akamych.buzzers.enums.UserRolesEnum;
import com.akamych.buzzers.repositories.GameRepository;
import com.akamych.buzzers.repositories.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final GameService gameService;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse createHost(HttpServletResponse response, User user) {

        if (user != null) {
            if (user.getHostingGame() != null) {
                gameService.deleteGame(user.getHostingGame());
            } else if (user.getPlayingGame() != null) {
                gameService.removePlayer(user);
            }
            userRepository.delete(user);
        }

        Game newGame = gameService.createGame();

        User newUser = User.builder()
                .role(UserRolesEnum.HOST.getRole())
                .hostingGame(newGame)
                .build();

        userRepository.saveAndFlush(newUser);

        jwtService.setJwtCookie(newUser, response);

        return AuthResponse.builder()
                .host(true)
                .game(newGame.getGameId())
                .build();
    }

    @Transactional
    public AuthResponse createPlayer(HttpServletResponse response, User user) {

        if (user != null) {
            if (user.getHostingGame() != null) {
                return AuthResponse.builder()
                        .game(user.getHostingGame().getGameId())
                        .host(true)
                        .build();
            }

            if (user.getPlayingGame() != null) {
                return AuthResponse.builder()
                        .game(user.getPlayingGame().getGameId())
                        .host(false)
                        .build();
            }
        }

        User newUser = User.builder()
                .role(UserRolesEnum.PLAYER.getRole())
                .build();

        userRepository.saveAndFlush(newUser);
        jwtService.setJwtCookie(newUser, response);

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
        if (game == null || game.isDeleted()) {
            return false;
        }

        user.setPlayingGame(game);
        user.setPlayerName(request.name());
        userRepository.saveAndFlush(user);

        game.getPlayers().add(user);
        gameRepository.saveAndFlush(game);

        return true;
    }

    private void deleteCurrentUser(User user) {

        if (user.getHostingGame() != null) {
            gameService.deleteGame(user.getHostingGame());
        }

        if (user != null) {
            userRepository.delete(user);
        }
    }

    public boolean logout(HttpServletResponse response, User user) {

        if (user == null) {
            return true;
        }

        if (user.getPlayingGame() != null) {
            gameService.removePlayer(user);
        }

        if (user.getHostingGame() != null) {
            gameService.deleteGame(user.getHostingGame());
        }

        userRepository.delete(user);
        SecurityContextHolder.clearContext();
        jwtService.deleteJwtCookie(response);

        return true;
    }


    @Transactional
    public GameInfoResponse getGameInfo(User user) {
        if (user == null || (user.getHostingGame() == null && user.getPlayingGame() == null)) {
            return GameInfoResponse.builder().isDeleted(true).build();
        }

        if (user.getPlayingGame() != null) {
            if (user.getPlayingGame().isDeleted()) {
                return GameInfoResponse.builder().isDeleted(true).build();
            }
            return gameService.getGameShortInfo(user.getPlayingGame(), user);
        }

        return gameService.getGameInfo(user.getHostingGame());
    }


    @Scheduled(fixedRate = 300000)
    @Transactional
    public void scheduledDeletion() {

        try {
            userRepository.deleteAllByHostingGameIsNullAndPlayingGameIsNull();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ZonedDateTime twoHoursAgo = ZonedDateTime.now().minusHours(2);
            userRepository.deleteAllByUpdatedAtBefore(twoHoursAgo);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
