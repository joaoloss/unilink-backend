package com.unilink.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.unilink.api.model.User;
import com.unilink.api.security.JwtUtil;
import com.unilink.api.dtos.UserRequestDTO;
import com.unilink.api.exception.LoginFailureException;
import com.unilink.api.service.UserService;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.ExampleObject;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints para autenticação de usuários")
public class AuthController {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserService userService;

    @PostMapping("/login")
    @Operation(
        summary = "Realizar login",
        description = "Autentica um usuário com email e senha, retornando um token JWT"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Login realizado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Map.class),
                examples = @ExampleObject(
                    name = "Login bem-sucedido",
                    value = "{\"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Credenciais inválidas",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = String.class),
                examples = @ExampleObject(
                    name = "Erro de autenticação",
                    value = "\"Login failed\""
                )
            )
        )
    })
    public ResponseEntity<?> login(
        @RequestBody 
        @Schema(description = "Dados de login do usuário", requiredMode = Schema.RequiredMode.REQUIRED)
        UserRequestDTO userRequestDTO
    ) {
        User user = userService.getUserByEmail(userRequestDTO.email());
        if(user == null) throw new LoginFailureException();

        if(passwordEncoder.matches(userRequestDTO.password(), user.getPassword())) {
            String token = JwtUtil.generateToken(user.getId().toString());
            return ResponseEntity.ok(Map.of("token", token));
        } else throw new LoginFailureException();
    }
}
