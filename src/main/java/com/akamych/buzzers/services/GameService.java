package com.akamych.buzzers.services;

import com.akamych.buzzers.entities.Game;
import com.akamych.buzzers.entities.User;
import com.akamych.buzzers.repositories.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;

    private Long generateId() {
        return ThreadLocalRandom.current().nextLong(100_000_000L, 1_000_000_000L);
    }

    public Game createGame() {

        Game newGame = Game.builder()
                .gameId(generateId())
                .build();

        int tryNumber = 0;
        boolean success = false;

        while (tryNumber++ < 6 && !success) {
            try {
                gameRepository.save(newGame);
                success = true;
            } catch (Exception e) {
                e.printStackTrace();
                newGame.setGameId(generateId());
            }
        }

        return newGame;

    }

    public Game getById(Long id) {
        return gameRepository.findByGameId(id).orElse(null);
    }

    public void removePlayer(User user) {
        Game game = user.getPlayingGame();
        List<User> players = game.getPlayers();
        game.setPlayers(players.stream().filter(iterate -> !iterate.equals(user)).collect(Collectors.toList()));
        gameRepository.saveAndFlush(game);
    }
}
