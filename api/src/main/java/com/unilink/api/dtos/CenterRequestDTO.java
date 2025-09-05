package com.unilink.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO for center requests")
public record CenterRequestDTO(
    @Schema(description = "Center name", example = "Technology Center")
    String name,
    @Schema(description = "Center URL", example = "https://technology-center.com")
    String centerUrl
) {
    public boolean isValidForCreation() {
        return name != null && !name.isBlank() && centerUrl != null && !centerUrl.isBlank();
    }
}
