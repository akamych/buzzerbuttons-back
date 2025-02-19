package com.akamych.buzzers.services;

import com.akamych.buzzers.entities.stats.StatsDaily;
import com.akamych.buzzers.repositories.stats.StatsDailyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class WebsocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public void informPlayersAboutHostLeaving(Long gameId) {
        messagingTemplate.convertAndSend("/topic/game/" + gameId + "/host-leaves", true);
    }

    public void informHostAboutPlayerLeaving(Long gameId, String playerName) {
        messagingTemplate.convertAndSend("/topic/game/" + gameId + "/player-quits", playerName);
    }

}
