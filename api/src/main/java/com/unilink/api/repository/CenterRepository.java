package com.unilink.api.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.unilink.api.model.Center;

public interface CenterRepository extends JpaRepository<Center, UUID> {
    
}
