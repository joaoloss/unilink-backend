package com.unilink.api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unilink.api.domain.admin.Admin;

public interface AdminRepository extends JpaRepository<Admin, UUID> {
    
    
    
}
