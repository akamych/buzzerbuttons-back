package com.akamych.buzzers.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "games", schema = "public")
public class Game {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(unique = true)
    private Long gameId;

    @OneToOne(mappedBy = "game")
    private User host;

    @OneToMany
    private List<User> players;

    private boolean isActive;

    @CreationTimestamp
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    private ZonedDateTime updatedAt;
}
