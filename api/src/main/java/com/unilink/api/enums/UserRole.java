package com.unilink.api.enums;

public enum UserRole {
    PROJECT_ADMIN("PROJECT_ADMIN"),
    SUPER_ADMIN("SUPER_ADMIN");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
