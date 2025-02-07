package com.akamych.buzzers.dtos;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record GameInfoResponse(List<String> players, Map<String, String> results, boolean isActive, boolean isAlreadyPressed, String activatedAt) {}
