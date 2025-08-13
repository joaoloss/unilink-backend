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
@Tag(name = "Centros", description = "Endpoints para gerenciamento de centros")
public class CenterController {
    private final CenterService centerService;

    @GetMapping
    @Operation(
        summary = "Listar todos os centros",
        description = "Retorna uma lista com todos os centros cadastrados no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de centros retornada com sucesso",
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
        summary = "Buscar centro por ID",
        description = "Retorna um centro específico baseado no ID fornecido"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Centro encontrado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Center.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Centro não encontrado"
        )
    })
    public ResponseEntity<Center> getCenterById(
        @Parameter(description = "ID único do centro", required = true)
        @PathVariable UUID id
    ) {
        return ResponseEntity.ok(this.centerService.getCenterById(id));
    }

    @PostMapping
    @Operation(
        summary = "Criar novo centro",
        description = "Cria um novo centro no sistema com os dados fornecidos"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Centro criado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Center.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos fornecidos"
        )
    })
    public ResponseEntity<Center> createCenter(
        @RequestBody 
        @Schema(description = "Dados do centro para criação", requiredMode = Schema.RequiredMode.REQUIRED)
        CenterRequestDTO centerRequest
    ) {
        if(!centerRequest.isValidForCreation()) {
            throw new InvalidFieldException("Invalid center data provided for creation.");
        }

        return ResponseEntity.ok(this.centerService.createCenter(centerRequest));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar centro",
        description = "Atualiza os dados de um centro existente baseado no ID fornecido"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Centro atualizado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Center.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Centro não encontrado"
        )
    })
    public ResponseEntity<Center> updateCenter(
        @Parameter(description = "ID único do centro", required = true)
        @PathVariable UUID id, 
        @RequestBody 
        @Schema(description = "Dados atualizados do centro", requiredMode = Schema.RequiredMode.REQUIRED)
        CenterRequestDTO updatedCenter
    ) {
        return ResponseEntity.ok(this.centerService.updateCenter(id, updatedCenter));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Excluir centro",
        description = "Remove um centro do sistema baseado no ID fornecido"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Centro excluído com sucesso"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Centro não encontrado"
        )
    })
    public ResponseEntity<Void> deleteCenter(
        @Parameter(description = "ID único do centro", required = true)
        @PathVariable UUID id
    ) {
        this.centerService.deleteCenter(id);
        return ResponseEntity.noContent().build();
    }
}
