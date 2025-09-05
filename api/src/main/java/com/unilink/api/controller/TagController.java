package com.unilink.api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unilink.api.dtos.TagRequestDTO;
import com.unilink.api.exception.InvalidFieldException;
import com.unilink.api.model.Tag;
import com.unilink.api.service.TagService;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tags", description = "Endpoints for tag management")
public class TagController {
    
    private final TagService tagService;

    @GetMapping
    @Operation(
        summary = "List all tags",
        description = "Returns a list of all tags registered in the system"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "List of tags successfully returned",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Tag.class)
            )
        )
    })
    public ResponseEntity<List<Tag>> getAllTags() {
        return ResponseEntity.ok(this.tagService.getAllTags());
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get tag by ID",
        description = "Returns a specific tag based on the provided ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Tag successfully found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Tag.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Tag not found"
        )
    })
    public ResponseEntity<Tag> getTagById(
        @Parameter(description = "Unique ID of the tag", required = true)
        @PathVariable UUID id
    ) {
        return ResponseEntity.ok(this.tagService.getTagById(id));
    }

    @PostMapping
    @Operation(
        summary = "Create new tag",
        description = "Creates a new tag in the system with the provided data"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Tag successfully created",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Tag.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid data provided"
        )
    })
    public ResponseEntity<Tag> createTag(
        @RequestBody 
        @Schema(description = "Tag data for creation", requiredMode = Schema.RequiredMode.REQUIRED)
        TagRequestDTO tagRequest
    ) {
        if(!tagRequest.isValidForCreation()) {
            throw new InvalidFieldException("Invalid tag data provided for creation.");
        }
        
        return ResponseEntity.ok(this.tagService.createTag(tagRequest));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Update tag",
        description = "Updates the data of an existing tag based on the provided ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Tag successfully updated",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Tag.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Tag not found"
        )
    })
    public ResponseEntity<Tag> updateTag(
        @Parameter(description = "Unique ID of the tag", required = true)
        @PathVariable UUID id, 
        @RequestBody 
        @Schema(description = "Updated tag data", requiredMode = Schema.RequiredMode.REQUIRED)
        TagRequestDTO updatedTag
    ) {
        return ResponseEntity.ok(this.tagService.updateTag(id, updatedTag));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete tag",
        description = "Removes a tag from the system based on the provided ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Tag successfully deleted"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Tag not found"
        )
    })
    public ResponseEntity<Void> deleteTag(
        @Parameter(description = "Unique ID of the tag", required = true)
        @PathVariable UUID id
    ) {
        this.tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}
