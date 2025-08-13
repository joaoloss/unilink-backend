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
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tags", description = "Endpoints para gerenciamento de tags")
public class TagController {
    
    private final TagService tagService;

    @GetMapping
    @Operation(
        summary = "Listar todas as tags",
        description = "Retorna uma lista com todas as tags cadastradas no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de tags retornada com sucesso",
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
        summary = "Buscar tag por ID",
        description = "Retorna uma tag específica baseada no ID fornecido"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Tag encontrada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Tag.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Tag não encontrada"
        )
    })
    public ResponseEntity<Tag> getTagById(
        @Parameter(description = "ID único da tag", required = true)
        @PathVariable UUID id
    ) {
        return ResponseEntity.ok(this.tagService.getTagById(id));
    }

    @PostMapping
    @Operation(
        summary = "Criar nova tag",
        description = "Cria uma nova tag no sistema com os dados fornecidos"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Tag criada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Tag.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos fornecidos"
        )
    })
    public ResponseEntity<Tag> createTag(
        @RequestBody 
        @Schema(description = "Dados da tag para criação", requiredMode = Schema.RequiredMode.REQUIRED)
        TagRequestDTO tagRequest
    ) {
        if(!tagRequest.isValidForCreation()) {
            throw new InvalidFieldException("Invalid tag data provided for creation.");
        }
        
        return ResponseEntity.ok(this.tagService.createTag(tagRequest));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar tag",
        description = "Atualiza os dados de uma tag existente baseada no ID fornecido"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Tag atualizada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Tag.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Tag não encontrada"
        )
    })
    public ResponseEntity<Tag> updateTag(
        @Parameter(description = "ID único da tag", required = true)
        @PathVariable UUID id, 
        @RequestBody 
        @Schema(description = "Dados atualizados da tag", requiredMode = Schema.RequiredMode.REQUIRED)
        TagRequestDTO updatedTag
    ) {
        return ResponseEntity.ok(this.tagService.updateTag(id, updatedTag));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Excluir tag",
        description = "Remove uma tag do sistema baseada no ID fornecido"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Tag excluída com sucesso"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Tag não encontrada"
        )
    })
    public ResponseEntity<Void> deleteTag(
        @Parameter(description = "ID único da tag", required = true)
        @PathVariable UUID id
    ) {
        this.tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}
