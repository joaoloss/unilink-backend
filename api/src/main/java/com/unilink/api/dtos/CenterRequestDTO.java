package com.unilink.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para requisições de centro")
public record CenterRequestDTO(
    @Schema(description = "Nome do centro", example = "Centro de Tecnologia")
    String name,
    @Schema(description = "URL do centro", example = "https://centro-tecnologia.com")
    String centerUrl
) {
    public boolean isValidForCreation() {
        return name != null && !name.isBlank() && centerUrl != null && !centerUrl.isBlank();
    }
}