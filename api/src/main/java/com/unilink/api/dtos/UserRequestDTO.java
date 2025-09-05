package com.unilink.api.dtos;

import com.unilink.api.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO for user requests")
public record UserRequestDTO(
    @Schema(description = "Full name of the user", example = "John Silva")
    String name,

    @Schema(description = "User email", example = "john.silva@email.com")
    String email,

    @Schema(description = "User password", example = "password123")
    String password,

    @Schema(description = "Role/function of the user in the system")
    UserRole role
) {
    public boolean isValidForCreation() {
        return name != null && !name.isBlank() &&
               email != null && !email.isBlank() &&
               password != null && !password.isBlank() &&
               role != null;
    }
}
