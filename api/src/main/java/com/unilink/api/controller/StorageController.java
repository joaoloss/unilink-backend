package com.unilink.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.unilink.api.service.R2StorageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/storage")
@RequiredArgsConstructor
public class StorageController {

    private final R2StorageService r2StorageService;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws Exception {
        String url = r2StorageService.upload(
            file.getInputStream(),
            file.getSize(),
            file.getOriginalFilename(),
            file.getContentType()
        );
        return ResponseEntity.ok(url);
    }
}


