package com.akamych.buzzers.enums;

import lombok.Getter;

@Getter
public enum UserRolesEnum {
    PLAYER("ROLE_PLAYER"),
    HOST("ROLE_HOST");

    private String role;

    UserRolesEnum(String role) {
        this.role = role;
    }

    public String getRoleWOPrefix() {
        return this.getRole().substring(5);
    }
}
