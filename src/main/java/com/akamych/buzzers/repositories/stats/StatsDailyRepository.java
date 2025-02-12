package com.akamych.buzzers.repositories.stats;

import com.akamych.buzzers.entities.Game;
import com.akamych.buzzers.entities.stats.StatsDaily;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StatsDailyRepository extends JpaRepository<StatsDaily, ZonedDateTime> {
    Optional<StatsDaily> findByDate(ZonedDateTime date);
}