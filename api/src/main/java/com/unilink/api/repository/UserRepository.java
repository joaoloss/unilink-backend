package com.unilink.api.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.unilink.api.model.User;

public interface UserRepository extends JpaRepository<User, UUID> {
    
}
