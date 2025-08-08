package com.unilink.api.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.unilink.api.model.Project;

public interface ProjectRepository extends JpaRepository<Project, UUID>, JpaSpecificationExecutor<Project> {
    public List<Project> findByCenterId(UUID centerId);
    
    public List<Project> findByOwnerId(UUID ownerId);
}
