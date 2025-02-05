package com.akamych.buzzers.dtos;

import lombok.Builder;

@Builder
public record JoinGameRequest(Long game, String name) {}
