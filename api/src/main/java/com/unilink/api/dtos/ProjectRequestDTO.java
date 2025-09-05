package com.unilink.api.dtos;

import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO for project requests")
public record ProjectRequestDTO(
    @Schema(description = "Project name", example = "School Management System")
    String name,

    @Schema(description = "Detailed description of the project", example = "A complete system for managing schools")
    String description,

    @Schema(description = "ID of the center responsible for the project")
    UUID centerId,

    @Schema(description = "ID of the user who owns the project")
    UUID ownerId,

    @Schema(description = "Indicates whether the project is open for applications", example = "true")
    boolean openForApplications,

    @Schema(description = "Project image URL", example = "https://example.com/project-image.jpg")
    String imgUrl,

    @Schema(description = "Project team size", example = "5")
    int teamSize,

    @Schema(description = "Array of tag IDs to be added to the project")
    UUID[] tagsToBeAdded,

    @Schema(description = "Array of tag IDs to be removed from the project")
    UUID[] tagsToBeRemoved,

    @Schema(description = "Image data in Base64 (optional)")
    String imageBase64,

    @Schema(description = "Image content type (optional, e.g., image/jpeg)")
    String imageContentType
) {
    public boolean isValidForCreation() {
        return name != null && !name.isBlank() &&
               description != null && !description.isBlank() &&
               ownerId != null;
    }
    
    public boolean hasImageData() {
        return imageBase64 != null && !imageBase64.isBlank();
    }
}
