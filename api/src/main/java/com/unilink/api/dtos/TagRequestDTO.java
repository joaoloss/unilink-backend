package com.unilink.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO for tag requests")
public record TagRequestDTO(
    @Schema(description = "Tag name", example = "Java")
    String name,

    @Schema(description = "Tag color in hexadecimal format", example = "#FF5733")
    String colorHex
) {
    public boolean isValidForCreation() {
        return name != null && !name.isBlank() && colorHex != null && !colorHex.isBlank();
    }
}
