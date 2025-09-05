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

import com.unilink.api.dtos.CenterRequestDTO;
import com.unilink.api.exception.InvalidFieldException;
import com.unilink.api.model.Center;
import com.unilink.api.service.CenterService;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/centers")
@RequiredArgsConstructor
@Tag(name = "Centers", description = "Endpoints for center management")
public class CenterController {
    private final CenterService centerService;

    @GetMapping
    @Operation(
        summary = "List all centers",
        description = "Returns a list of all centers registered in the system"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "List of centers successfully returned",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Center.class)
            )
        )
    })
    public ResponseEntity<List<Center>> getAllCenters() {
        return ResponseEntity.ok(this.centerService.getAllCenters());
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get center by ID",
        description = "Returns a specific center based on the provided ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Center successfully found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Center.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Center not found"
        )
    })
    public ResponseEntity<Center> getCenterById(
        @Parameter(description = "Unique ID of the center", required = true)
        @PathVariable UUID id
    ) {
        return ResponseEntity.ok(this.centerService.getCenterById(id));
    }

    @PostMapping
    @Operation(
        summary = "Create new center",
        description = "Creates a new center in the system with the provided data"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Center successfully created",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Center.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid data provided"
        )
    })
    public ResponseEntity<Center> createCenter(
        @RequestBody 
        @Schema(description = "Data of the center to be created", requiredMode = Schema.RequiredMode.REQUIRED)
        CenterRequestDTO centerRequest
    ) {
        if(!centerRequest.isValidForCreation()) {
            throw new InvalidFieldException("Invalid center data provided for creation.");
        }

        return ResponseEntity.ok(this.centerService.createCenter(centerRequest));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Update center",
        description = "Updates the data of an existing center based on the provided ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Center successfully updated",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Center.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Center not found"
        )
    })
    public ResponseEntity<Center> updateCenter(
        @Parameter(description = "Unique ID of the center", required = true)
        @PathVariable UUID id, 
        @RequestBody 
        @Schema(description = "Updated center data", requiredMode = Schema.RequiredMode.REQUIRED)
        CenterRequestDTO updatedCenter
    ) {
        return ResponseEntity.ok(this.centerService.updateCenter(id, updatedCenter));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete center",
        description = "Removes a center from the system based on the provided ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Center successfully deleted"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Center not found"
        )
    })
    public ResponseEntity<Void> deleteCenter(
        @Parameter(description = "Unique ID of the center", required = true)
        @PathVariable UUID id
    ) {
        this.centerService.deleteCenter(id);
        return ResponseEntity.noContent().build();
    }
}
