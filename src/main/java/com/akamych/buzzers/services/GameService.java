package com.akamych.buzzers.services;

import com.akamych.buzzers.entities.Game;
import com.akamych.buzzers.repositories.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;

    private Long generateId() {
        Random random = new Random();
        return 1L + (random.nextLong() & Long.MAX_VALUE);
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
}
