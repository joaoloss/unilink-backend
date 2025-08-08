package com.unilink.api.DTO;

public record TagRequestDTO(
    String name,
    String colorHex
) {
    public boolean isValidForCreation() {
        return name != null && !name.isBlank() && colorHex != null && !colorHex.isBlank();
    }
} 