package com.akamych.buzzers.repositories;

import com.akamych.buzzers.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GameRepository extends JpaRepository<Game, UUID> {
    Optional<Game> findByGameId(long gameId);
}