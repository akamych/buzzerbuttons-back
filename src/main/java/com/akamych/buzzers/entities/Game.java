package com.akamych.buzzers.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.ZonedDateTime;
import java.util.*;

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

    @OneToOne(mappedBy = "hostingGame", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private User host;

    private String winnerName;

    @OneToMany(mappedBy = "playingGame", orphanRemoval = true, fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
    @Builder.Default
    private List<User> players = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private Map<String, String> results = new HashMap<>();
    private boolean isActive;
    private boolean isDeleted;
    @CreationTimestamp
    private ZonedDateTime createdAt;
    @UpdateTimestamp
    private ZonedDateTime updatedAt;
    private ZonedDateTime activatedAt;
    private ZonedDateTime nextRoundAt;
}
