package com.unilink.api.DTO;

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
    
}
