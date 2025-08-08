package com.unilink.api.dtos;

import java.util.UUID;

public record ProjectRequestDTO(
    String name,
    String description,
    UUID centerId,
    UUID ownerId,
    boolean openForApplications,
    String imgUrl,
    int teamSize,
    UUID[] tagsToBeAdded,
    UUID[] tagsToBeRemoved
) {
    public boolean isValidForCreation() {
        return name != null && !name.isBlank() &&
               description != null && !description.isBlank() &&
               ownerId != null;
    }
}
