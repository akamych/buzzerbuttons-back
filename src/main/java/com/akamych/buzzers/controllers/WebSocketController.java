package com.akamych.buzzers.controllers;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Controller
public class WebSocketController {

    @MessageMapping("/game/{gameId}") // Handle messages sent to /app/game/{gameId}
    @SendTo("/topic/game/{gameId}")   // Broadcast messages to /topic/game/{gameId}
    public String handleGameMessage(String message, @DestinationVariable String id) {
        System.out.println("Received message for host " + id + ": " + message);
        return "Received message for host " + id + ": " + message;
    }

    @MessageMapping("/game") // Handle messages sent to /app/game/{gameId}
    @SendTo("/topic/game")   // Broadcast messages to /topic/game/{gameId}
    public String handleGameMessage1(@Payload String message) {
        System.out.println("Received message for host " + message);
        return "Received message for host " + message;
    }
}