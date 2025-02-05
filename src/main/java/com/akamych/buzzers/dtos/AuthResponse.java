package com.akamych.buzzers.dtos;

import lombok.Builder;

@Builder
public record AuthResponse (Long game, Boolean host) {}
