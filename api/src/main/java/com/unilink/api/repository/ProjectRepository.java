package com.unilink.api.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.unilink.api.model.Project;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
    public List<Project> findByCenterId(UUID centerId);
    
    public List<Project> findByOwnerId(UUID ownerId);
}
