package com.unilink.api.dtos;

import com.unilink.api.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para requisições de usuário")
public record UserRequestDTO(
    @Schema(description = "Nome completo do usuário", example = "João Silva")
    String name,
    @Schema(description = "Email do usuário", example = "joao.silva@email.com")
    String email,
    @Schema(description = "Senha do usuário", example = "senha123")
    String password,
    @Schema(description = "Papel/função do usuário no sistema")
    UserRole role
) {
    public boolean isValidForCreation() {
        return name != null && !name.isBlank() &&
               email != null && !email.isBlank() &&
               password != null && !password.isBlank() &&
               role != null;
    }
}
