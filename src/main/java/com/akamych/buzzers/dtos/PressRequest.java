package com.akamych.buzzers.dtos;

import lombok.Builder;

@Builder
public record PressRequest(String playerName, String pressedAt) {}
