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

import com.unilink.api.dtos.ProjectRequestDTO;
import com.unilink.api.exception.InvalidFieldException;
import com.unilink.api.model.Project;
import com.unilink.api.repository.filters.ProjectQueryFilter;
import com.unilink.api.service.ProjectService;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "Projects", description = "Endpoints for project management")
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping
    @Operation(
        summary = "List projects",
        description = "Returns a list of projects. Can be filtered using ProjectQueryFilter in the request body"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "List of projects successfully returned",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Project.class)
            )
        )
    })
    public ResponseEntity<List<Project>> getAllProjects(
        @RequestBody(required = false) 
        @Schema(description = "Optional filters to search for projects")
        ProjectQueryFilter projectFilterSpec
    ) {
        if(projectFilterSpec == null) return ResponseEntity.ok(this.projectService.getAllProjects(null));
        return ResponseEntity.ok(this.projectService.getAllProjects(projectFilterSpec.toSpecification()));
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get project by ID",
        description = "Returns a specific project based on the provided ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Project successfully found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Project.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Project not found"
        )
    })
    public ResponseEntity<Project> getProjectById(
        @Parameter(description = "Unique project ID", required = true)
        @PathVariable UUID id
    ) {
        return ResponseEntity.ok(this.projectService.getProjectById(id));
    }

    @PostMapping
    @Operation(
        summary = "Create new project",
        description = "Creates a new project in the system with the provided data. Supports Base64 image upload."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Project successfully created",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Project.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid data provided"
        )
    })
    public ResponseEntity<Project> createProject(
        @RequestBody 
        @Schema(description = "Project data for creation", requiredMode = Schema.RequiredMode.REQUIRED)
        ProjectRequestDTO projectRequest
    ) {
        if(!projectRequest.isValidForCreation()) {
            throw new InvalidFieldException("Invalid project data provided for creation.");
        }
        
        return ResponseEntity.ok(this.projectService.createProject(projectRequest));
    }
    
    @PutMapping("/{id}")
    @Operation(
        summary = "Update project",
        description = "Updates the data of an existing project based on the provided ID. Supports Base64 image upload."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Project successfully updated",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Project.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Project not found"
        )
    })
    public Project updateProject(
        @Parameter(description = "Unique project ID", required = true)
        @PathVariable UUID id, 
        @RequestBody 
        @Schema(description = "Updated project data", requiredMode = Schema.RequiredMode.REQUIRED)
        ProjectRequestDTO updatedProject
    ) {
        return this.projectService.updateProject(id, updatedProject);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete project",
        description = "Removes a project from the system based on the provided ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Project successfully deleted"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Project not found"
        )
    })
    public ResponseEntity<Void> deleteProject(
        @Parameter(description = "Unique project ID", required = true)
        @PathVariable UUID id
    ) {
        this.projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
