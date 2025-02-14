package com.akamych.buzzers.entities.stats;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigInteger;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "stats_daily", schema = "public")
public class StatsDaily {

    @Id
    private ZonedDateTime date;

    @Column
    @Builder.Default
    private BigInteger hosts = BigInteger.ZERO;

    @Column
    @Builder.Default
    private BigInteger players = BigInteger.ZERO;

    @Column
    @Builder.Default
    private BigInteger games = BigInteger.ZERO;

    @Column
    @Builder.Default
    private BigInteger gamesActivated = BigInteger.ZERO;

    @Column
    @Builder.Default
    private BigInteger rounds = BigInteger.ZERO;

    @Column
    @Builder.Default
    private BigInteger buttonsPressed = BigInteger.ZERO;

}
