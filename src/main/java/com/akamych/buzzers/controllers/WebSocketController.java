package com.akamych.buzzers.controllers;

import com.akamych.buzzers.dtos.GameInfoResponse;
import com.akamych.buzzers.dtos.GameStatusResponse;
import com.akamych.buzzers.dtos.PressRequest;
import com.akamych.buzzers.entities.User;
import com.akamych.buzzers.services.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.ZonedDateTime;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final GameService gameService;

    @MessageMapping("/game/{gameId}/new-player")
    @SendTo("/topic/game/{gameId}/new-player")
    public String addPlayerToTheGame(@RequestBody String playerName) {
        return playerName;
    }

    @MessageMapping("/game/{gameId}/player-quits")
    @SendTo("/topic/game/{gameId}/player-quits")
    public String removePlayer(@RequestBody String playerName) {
        return playerName;
    }

    @MessageMapping("/game/{gameId}/activate")
    @SendTo("/topic/game/{gameId}/activate")
    public String activateGame(@RequestBody String activatedAt, @DestinationVariable long gameId) {
        return gameService.activateGame(gameId, activatedAt) ? activatedAt : null;
    }

    @MessageMapping("/game/{gameId}/next-round")
    @SendTo("/topic/game/{gameId}/next-round")
    public String startNextRound(@RequestBody String nextRoundAt, @DestinationVariable long gameId) {
        return gameService.startNextRound(gameId, nextRoundAt) ? nextRoundAt : null;
    }

    @MessageMapping("/game/{gameId}/press")
    @SendTo("/topic/game/{gameId}/results")
    public Map<String, String> savePressResult(@RequestBody PressRequest request, @DestinationVariable long gameId) {
        return gameService.savePressResult(gameId, request);
    }

    @MessageMapping("/game/{gameId}/game-status")
    @SendTo("/topic/game/{gameId}/game-status")
    public GameStatusResponse checkGameStatus(@DestinationVariable long gameId) {
        return gameService.checkGameStatus(gameId);
    }

}