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

import com.unilink.api.dtos.UserRequestDTO;
import com.unilink.api.exception.InvalidFieldException;
import com.unilink.api.model.User;
import com.unilink.api.service.UserService;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários")
public class UserController {
    private final UserService userService;

    @GetMapping
    @Operation(
        summary = "Listar todos os usuários",
        description = "Retorna uma lista com todos os usuários cadastrados no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de usuários retornada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = User.class)
            )
        )
    })
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(this.userService.getAllUsers());
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar usuário por ID",
        description = "Retorna um usuário específico baseado no ID fornecido"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuário encontrado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = User.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuário não encontrado"
        )
    })
    public ResponseEntity<User> getUserById(
        @Parameter(description = "ID único do usuário", required = true)
        @PathVariable UUID id
    ) {
        return ResponseEntity.ok(this.userService.getUserById(id));
    }

    @PostMapping
    @Operation(
        summary = "Criar novo usuário",
        description = "Cria um novo usuário no sistema com os dados fornecidos"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuário criado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = User.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos fornecidos"
        )
    })
    public ResponseEntity<User> createUser(
        @RequestBody 
        @Schema(description = "Dados do usuário para criação", requiredMode = Schema.RequiredMode.REQUIRED)
        UserRequestDTO userRequest
    ) {
        if(!userRequest.isValidForCreation()) {
            throw new InvalidFieldException("Invalid user data provided for creation.");
        }
        
        return ResponseEntity.ok(this.userService.createUser(userRequest));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar usuário",
        description = "Atualiza os dados de um usuário existente baseado no ID fornecido"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuário atualizado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = User.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuário não encontrado"
        )
    })
    public ResponseEntity<User> updateUser(
        @Parameter(description = "ID único do usuário", required = true)
        @PathVariable UUID id, 
        @RequestBody 
        @Schema(description = "Dados atualizados do usuário", requiredMode = Schema.RequiredMode.REQUIRED)
        UserRequestDTO updatedUser
    ) {
        return ResponseEntity.ok(this.userService.updateUser(id, updatedUser));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Excluir usuário",
        description = "Remove um usuário do sistema baseado no ID fornecido"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Usuário excluído com sucesso"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuário não encontrado"
        )
    })
    public ResponseEntity<Void> deleteUser(
        @Parameter(description = "ID único do usuário", required = true)
        @PathVariable UUID id
    ) {
        this.userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
