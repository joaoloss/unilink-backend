package com.unilink.api.seed;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.unilink.api.dtos.UserRequestDTO;
import com.unilink.api.enums.UserRole;
import com.unilink.api.repository.UserRepository;
import com.unilink.api.service.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SuperAdminInitialSeeder implements CommandLineRunner {

    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if(userRepository.count() == 0) {
            UserRequestDTO initialSuperAdmin = new UserRequestDTO("superAdmin", "super.admin@dev.com", "admin123!", UserRole.SUPER_ADMIN);
            userService.createUser(initialSuperAdmin);
        }
    }
    
}
