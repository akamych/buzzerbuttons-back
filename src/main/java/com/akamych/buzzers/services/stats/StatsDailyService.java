package com.akamych.buzzers.services.stats;

import com.akamych.buzzers.entities.stats.StatsDaily;
import com.akamych.buzzers.repositories.stats.StatsDailyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class StatsDailyService {

    private final StatsDailyRepository statsDailyRepository;

    private final ReentrantLock creationLock = new ReentrantLock();

    private StatsDaily createTodayStatsDaily(ZonedDateTime today) {
        return statsDailyRepository.saveAndFlush(StatsDaily.builder().date(today).build());
    }

    private StatsDaily getTodayStatsDaily() {
        ZonedDateTime today = LocalDate.now(ZoneOffset.UTC).atStartOfDay(ZoneOffset.UTC);

        Optional<StatsDaily> optionalStatsDaily = statsDailyRepository.findByDate(today);
        if (optionalStatsDaily.isPresent()) {
            return optionalStatsDaily.get();
        }

        creationLock.lock();

        optionalStatsDaily = statsDailyRepository.findByDate(today);
        if (optionalStatsDaily.isPresent()) {
            return optionalStatsDaily.get();
        }

        try {
            return createTodayStatsDaily(today);
        } finally {
            creationLock.unlock();
        }
    }

    @Transactional
    public void increaseHostsCounter() {
        StatsDaily statsDaily = getTodayStatsDaily();
        statsDaily.setHosts(statsDaily.getHosts().add(BigInteger.ONE));
        statsDailyRepository.save(statsDaily);
    }

    @Transactional
    public void increasePlayersCounter() {
        StatsDaily statsDaily = getTodayStatsDaily();
        statsDaily.setPlayers(statsDaily.getPlayers().add(BigInteger.ONE));
        statsDailyRepository.save(statsDaily);
    }

    @Transactional
    public void increaseGamesCounter() {
        StatsDaily statsDaily = getTodayStatsDaily();
        statsDaily.setGames(statsDaily.getGames().add(BigInteger.ONE));
        statsDailyRepository.save(statsDaily);
    }

    @Transactional
    public void increaseGamesActivatedCounter() {
        StatsDaily statsDaily = getTodayStatsDaily();
        statsDaily.setGamesActivated(statsDaily.getGamesActivated().add(BigInteger.ONE));
        statsDailyRepository.save(statsDaily);
    }

    @Transactional
    public void increaseRoundsCounter() {
        StatsDaily statsDaily = getTodayStatsDaily();
        statsDaily.setRounds(statsDaily.getRounds().add(BigInteger.ONE));
        statsDailyRepository.save(statsDaily);
    }

    @Transactional
    public void increaseButtonsPressedCounter() {
        StatsDaily statsDaily = getTodayStatsDaily();
        statsDaily.setButtonsPressed(statsDaily.getButtonsPressed().add(BigInteger.ONE));
        statsDailyRepository.save(statsDaily);
    }



}
