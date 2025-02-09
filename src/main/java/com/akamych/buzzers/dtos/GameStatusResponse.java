package com.akamych.buzzers.dtos;

import lombok.Builder;

@Builder
public record GameStatusResponse(boolean isFinished, String winner) {
    public static GameStatusResponse getDeletedGameResponse() {
        return GameStatusResponse.builder().isFinished(true).build();
    }
}
