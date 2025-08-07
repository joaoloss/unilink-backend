package com.unilink.api.DTO;

import com.unilink.api.model.UserRole;

public record UserRequestDTO(
    String name,
    String email,
    String password,
    UserRole role
) {
}
