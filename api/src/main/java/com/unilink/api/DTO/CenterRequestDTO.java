package com.unilink.api.DTO;

public record CenterRequestDTO(
    String name,
    String centerUrl
) {
    public boolean isValidForCreation() {
        return name != null && !name.isBlank() && centerUrl != null && !centerUrl.isBlank();
    }
}