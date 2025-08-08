package com.unilink.api.dtos;

import com.unilink.api.enums.UserRole;

public record UserRequestDTO(
    String name,
    String email,
    String password,
    UserRole role
) {
    public boolean isValidForCreation() {
        return name != null && !name.isBlank() &&
               email != null && !email.isBlank() &&
               password != null && !password.isBlank() &&
               role != null;
    }
}
