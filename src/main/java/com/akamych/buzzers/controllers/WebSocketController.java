package com.akamych.buzzers.controllers;

import com.akamych.buzzers.entities.User;
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

@Controller
public class WebSocketController {

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
    public String activateGame(@DestinationVariable long gameId) {
        // TODO: set start time
        return "1";
    }
}