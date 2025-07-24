package com.unilink.api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unilink.api.domain.project.Project;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
 
}
