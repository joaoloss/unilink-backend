package com.unilink.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/test")
@Tag(name = "Test", description = "Test endpoints")
public class TestController {

    @GetMapping
    @Operation(
        summary = "Connectivity test",
        description = "Simple endpoint to check if the API is running"
    )
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("API is working!");
    }
}
