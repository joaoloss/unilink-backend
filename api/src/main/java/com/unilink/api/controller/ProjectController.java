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

import com.unilink.api.DTO.ProjectRequestDTO;
import com.unilink.api.exceptions.InvalidFieldException;
import com.unilink.api.model.Project;
import com.unilink.api.service.ProjectService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        return ResponseEntity.ok(this.projectService.getAllProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable UUID id) {
        return ResponseEntity.ok(this.projectService.getProjectById(id));
    }

    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody ProjectRequestDTO projectRequest) {
        if(!projectRequest.isValidForCreation()) {
            throw new InvalidFieldException("Invalid project data provided for creation.");
        }
        
        return ResponseEntity.ok(this.projectService.createProject(projectRequest));
    }
    
    @PutMapping("/{id}")
    public Project updateProject(@PathVariable UUID id, @RequestBody ProjectRequestDTO updatedProject) {
        return this.projectService.updateProject(id, updatedProject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID id) {
        this.projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}   
