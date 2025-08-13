package com.unilink.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/test")
@Tag(name = "Teste", description = "Endpoints de teste")
public class TestController {

    @GetMapping
    @Operation(
        summary = "Teste de conectividade",
        description = "Endpoint simples para testar se a API está funcionando"
    )
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("API está funcionando!");
    }
}
