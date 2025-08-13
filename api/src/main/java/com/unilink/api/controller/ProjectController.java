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

import com.unilink.api.dtos.ImageUploadDTO;
import com.unilink.api.dtos.ProjectRequestDTO;
import com.unilink.api.exception.InvalidFieldException;
import com.unilink.api.model.Project;
import com.unilink.api.repository.filters.ProjectQueryFilter;
import com.unilink.api.service.ProjectService;
import com.unilink.api.service.R2StorageService;

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
@Tag(name = "Projetos", description = "Endpoints para gerenciamento de projetos")
public class ProjectController {
    private final ProjectService projectService;
    private final R2StorageService r2StorageService;

    @GetMapping
    @Operation(
        summary = "Listar projetos",
        description = "Retorna uma lista de projetos. Pode ser filtrada usando ProjectQueryFilter no body da requisição"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de projetos retornada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Project.class)
            )
        )
    })
    public ResponseEntity<List<Project>> getAllProjects(
        @RequestBody(required = false) 
        @Schema(description = "Filtros opcionais para buscar projetos")
        ProjectQueryFilter projectFilterSpec
    ) {
        if(projectFilterSpec == null) return ResponseEntity.ok(this.projectService.getAllProjects(null));
        return ResponseEntity.ok(this.projectService.getAllProjects(projectFilterSpec.toSpecification()));
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar projeto por ID",
        description = "Retorna um projeto específico baseado no ID fornecido"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Projeto encontrado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Project.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Projeto não encontrado"
        )
    })
    public ResponseEntity<Project> getProjectById(
        @Parameter(description = "ID único do projeto", required = true)
        @PathVariable UUID id
    ) {
        return ResponseEntity.ok(this.projectService.getProjectById(id));
    }

    @PostMapping
    @Operation(
        summary = "Criar novo projeto",
        description = "Cria um novo projeto no sistema com os dados fornecidos"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Projeto criado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Project.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos fornecidos"
        )
    })
    public ResponseEntity<Project> createProject(
        @RequestBody 
        @Schema(description = "Dados do projeto para criação", requiredMode = Schema.RequiredMode.REQUIRED)
        ProjectRequestDTO projectRequest
    ) {
        if(!projectRequest.isValidForCreation()) {
            throw new InvalidFieldException("Invalid project data provided for creation.");
        }
        
        return ResponseEntity.ok(this.projectService.createProject(projectRequest));
    }
    
    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar projeto",
        description = "Atualiza os dados de um projeto existente baseado no ID fornecido"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Projeto atualizado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Project.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Projeto não encontrado"
        )
    })
    public Project updateProject(
        @Parameter(description = "ID único do projeto", required = true)
        @PathVariable UUID id, 
        @RequestBody 
        @Schema(description = "Dados atualizados do projeto", requiredMode = Schema.RequiredMode.REQUIRED)
        ProjectRequestDTO updatedProject
    ) {
        return this.projectService.updateProject(id, updatedProject);
    }

    @PostMapping("/{id}/image")
    @Operation(
        summary = "Upload de imagem do projeto",
        description = "Faz upload de uma imagem em Base64 para o projeto especificado"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Imagem enviada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Project.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados de imagem inválidos"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Projeto não encontrado"
        )
    })
    public ResponseEntity<Project> uploadProjectImage(
        @Parameter(description = "ID único do projeto", required = true)
        @PathVariable UUID id,
        @RequestBody 
        @Schema(description = "Dados da imagem em Base64", requiredMode = Schema.RequiredMode.REQUIRED)
        ImageUploadDTO imageUpload
    ) {
        if (!imageUpload.isValidBase64()) {
            throw new InvalidFieldException("Dados de imagem inválidos");
        }

        String fileName = imageUpload.fileName() != null ? imageUpload.fileName() : "project-image";
        String imageUrl = r2StorageService.uploadBase64(
            imageUpload.getBase64Data(),
            fileName,
            imageUpload.getContentType()
        );

        Project updatedProject = projectService.updateProjectImage(id, imageUrl);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{id}/image")
    @Operation(
        summary = "Remover imagem do projeto",
        description = "Remove a imagem atual do projeto especificado"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Imagem removida com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Project.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Projeto não encontrado"
        )
    })
    public ResponseEntity<Project> removeProjectImage(
        @Parameter(description = "ID único do projeto", required = true)
        @PathVariable UUID id
    ) {
        Project updatedProject = projectService.updateProjectImage(id, null);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Excluir projeto",
        description = "Remove um projeto do sistema baseado no ID fornecido"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Projeto excluído com sucesso"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Projeto não encontrado"
        )
    })
    public ResponseEntity<Void> deleteProject(
        @Parameter(description = "ID único do projeto", required = true)
        @PathVariable UUID id
    ) {
        this.projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}