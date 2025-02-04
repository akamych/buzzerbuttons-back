package com.akamych.buzzers.dtos.responses;

import lombok.Builder;

@Builder
public record AuthResponse (Long game, Boolean host) {}
