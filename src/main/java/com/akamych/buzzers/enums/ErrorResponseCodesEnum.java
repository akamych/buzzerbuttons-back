package com.akamych.buzzers.enums;

import lombok.Getter;

@Getter
public enum ErrorResponseCodesEnum {
    NO_SUCH_GAME(427),
    DOUBLE_PLAYER_NAME(428);

    private int code;

    ErrorResponseCodesEnum(int code) {
        this.code = code;
    }

}
