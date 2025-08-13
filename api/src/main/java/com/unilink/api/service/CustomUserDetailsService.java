package com.unilink.api.service;

import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.unilink.api.model.User;
import com.unilink.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findById(UUID.fromString(username))
            .orElseThrow(() -> new UsernameNotFoundException("User not found by ID"));

        String role = user.getRole().name(); // ex.: "SUPER_ADMIN"

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getId().toString())
                .password(user.getPassword()) // já codificada (BCrypt etc.)
                .roles(role)                    // adiciona prefixo ROLE_ automaticamente
                .build();
    }
}