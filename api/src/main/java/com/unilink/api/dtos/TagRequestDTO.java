package com.unilink.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para requisições de tag")
public record TagRequestDTO(
    @Schema(description = "Nome da tag", example = "Java")
    String name,
    @Schema(description = "Cor da tag em formato hexadecimal", example = "#FF5733")
    String colorHex
) {
    public boolean isValidForCreation() {
        return name != null && !name.isBlank() && colorHex != null && !colorHex.isBlank();
    }
} 