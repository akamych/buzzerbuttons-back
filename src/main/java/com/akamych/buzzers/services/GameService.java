package com.akamych.buzzers.services;

import com.akamych.buzzers.dtos.GameInfoResponse;
import com.akamych.buzzers.dtos.GameStatusResponse;
import com.akamych.buzzers.dtos.PressRequest;
import com.akamych.buzzers.entities.Game;
import com.akamych.buzzers.entities.User;
import com.akamych.buzzers.repositories.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final ReentrantLock gameResultsLock = new ReentrantLock();

    private Long generateId() {
        return ThreadLocalRandom.current().nextLong(100_000_000L, 1_000_000_000L);
    }

    @Transactional
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

    @Transactional
    public void removePlayer(User user) {
        Game game = user.getPlayingGame();
        List<User> players = game.getPlayers();
        game.setPlayers(players.stream().filter(iterate -> !iterate.equals(user)).collect(Collectors.toList()));
        gameRepository.saveAndFlush(game);
    }

    @Transactional
    public boolean activateGame(long gameId, String activatedAt) {

        Optional<Game> optionalGame = gameRepository.findByGameId(gameId);
        if (optionalGame.isEmpty()) {
            return false;
        }

        Game game = optionalGame.get();
        game.setActive(true);
        game.setActivatedAt(ZonedDateTime.parse(activatedAt));
        gameRepository.saveAndFlush(game);

        return true;
    }

    @Transactional
    public boolean startNextRound(long gameId, String nextRoundAt) {

        Optional<Game> optionalGame = gameRepository.findByGameId(gameId);
        if (optionalGame.isEmpty()) {
            return false;
        }

        Game game = optionalGame.get();
        game.setActive(false);
        game.setNextRoundAt(ZonedDateTime.parse(nextRoundAt));
        game.setResults(new HashMap<>());
        game.setWinnerName(null);
        gameRepository.saveAndFlush(game);

        return true;
    }

    @Transactional
    public Map<String, String> savePressResult(long gameId, PressRequest request) {

        Optional<Game> optionalGame = gameRepository.findByGameId(gameId);
        if (optionalGame.isEmpty()) {
            return null;
        }

        gameResultsLock.lock();

        try {

            Game game = optionalGame.get();
            if (!game.isActive()) {
                return null;
            }

            Map<String, String> results = game.getResults();

            if (results != null && results.containsKey(request.playerName())) {
                return results;
            }

            if (results == null) {
                results = new HashMap<>();
            }

            results.put(request.playerName(), ZonedDateTime.parse(request.pressedAt()).toString());
            game.setResults(results);

            if (results.size() == 1) {
                game.setWinnerName(request.playerName());
            }
            gameRepository.saveAndFlush(game);

            return results;
        } finally {
            gameResultsLock.unlock();
        }
    }

    @Transactional
    public GameInfoResponse getGameInfo(Game game) {

        return GameInfoResponse.builder()
                .activatedAt(game.getActivatedAt() == null ? null : game.getActivatedAt().toString())
                .players(game.getPlayers() == null
                        ? new ArrayList<>()
                        : game.getPlayers().stream()
                        .map(User::getPlayerName)
                        .collect(Collectors.toList()))
                .results(game.getResults())
                .isActive(game.isActive())
                .build();

    }

    @Transactional
    public GameStatusResponse checkGameStatus(long gameId) {
        Optional<Game> optionalGame = gameRepository.findByGameId(gameId);
        if (optionalGame.isEmpty()) {
            return GameStatusResponse.getDeletedGameResponse();
        }

        return GameStatusResponse.builder()
                .isFinished(optionalGame.get().isDeleted())
                .winner(optionalGame.get().getWinnerName())
                .build();
    }

    @Transactional
    public void deleteGame(Game game) {
        game.setDeleted(true);
        gameRepository.saveAndFlush(game);
        gameRepository.delete(game);
    }


    @Transactional
    public GameInfoResponse getGameShortInfo(Game game, User user) {

        boolean isAlreadyPressed = game.getResults() == null
                ? false
                : game.getResults().keySet()
                    .stream()
                    .filter(playerName -> playerName.equals(user.getPlayerName()))
                    .findFirst().isPresent();

        return GameInfoResponse.builder()
                .isActive(game.isActive())
                .isAlreadyPressed(isAlreadyPressed)
                .build();

    }

    @Scheduled(fixedRate = 300000)
    @Transactional
    public void scheduledDeletion() {

        try {
            gameRepository.deleteAllByIsDeletedTrue();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            gameRepository.deleteAllByHostIsNull();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ZonedDateTime twoHoursAgo = ZonedDateTime.now().minusHours(2);
            gameRepository.deleteAllByUpdatedAtBefore(twoHoursAgo);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
