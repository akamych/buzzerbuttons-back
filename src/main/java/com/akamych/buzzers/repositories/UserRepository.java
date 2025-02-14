package com.akamych.buzzers.repositories;

import com.akamych.buzzers.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    void delete(User user);
    void deleteAllByHostingGameIsNullAndPlayingGameIsNull();
    void deleteAllByUpdatedAtBefore(ZonedDateTime timeThreshold);
}