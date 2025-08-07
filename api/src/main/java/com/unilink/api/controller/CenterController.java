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

import com.unilink.api.DTO.CenterRequestDTO;
import com.unilink.api.model.Center;
import com.unilink.api.service.CenterService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/centers")
@RequiredArgsConstructor
public class CenterController {
    private final CenterService centerService;

    @GetMapping
    public ResponseEntity<List<Center>> getAllCenters() {
        return ResponseEntity.ok(this.centerService.getAllCenters());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Center> getCenterById(@PathVariable UUID id) {
        return ResponseEntity.ok(this.centerService.getCenterById(id));
    }

    @PostMapping
    public ResponseEntity<Center> createCenter(@RequestBody CenterRequestDTO centerRequest) {
        System.out.println("Creating center with request: " + centerRequest);
        return ResponseEntity.ok(this.centerService.createCenter(centerRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Center> updateCenter(@PathVariable UUID id, @RequestBody CenterRequestDTO updatedCenter) {
        return ResponseEntity.ok(this.centerService.updateCenter(id, updatedCenter));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCenter(@PathVariable UUID id) {
        this.centerService.deleteCenter(id);
        return ResponseEntity.noContent().build();
    }
}
