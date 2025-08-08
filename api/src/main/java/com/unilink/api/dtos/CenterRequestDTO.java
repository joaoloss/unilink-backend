package com.unilink.api.dtos;

public record CenterRequestDTO(
    String name,
    String centerUrl
) {
    public boolean isValidForCreation() {
        return name != null && !name.isBlank() && centerUrl != null && !centerUrl.isBlank();
    }
}