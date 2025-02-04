package com.akamych.buzzers.enums;

public enum UserRolesEnum {
    PLAYER("ROLE_PLAYER"),
    HOST("ROLE_HOST");

    private String role;

    UserRolesEnum(String role) {
        this.role = role;
    }

    public String getRole() {
        return this.getRole();
    }

    public String getRoleWOPrefix() {
        return this.getRole().substring(5);
    }
}
