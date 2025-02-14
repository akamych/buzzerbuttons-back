package com.akamych.buzzers.repositories.stats;

import com.akamych.buzzers.entities.stats.StatsDaily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Optional;

@Repository
public interface StatsDailyRepository extends JpaRepository<StatsDaily, ZonedDateTime> {
    Optional<StatsDaily> findByDate(ZonedDateTime date);
}