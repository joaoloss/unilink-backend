package com.unilink.api.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.unilink.api.model.Tag;

public interface TagRepository extends JpaRepository<Tag, UUID> {
    
}