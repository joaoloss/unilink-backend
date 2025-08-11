package com.unilink.api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unilink.api.dtos.UserRequestDTO;
import com.unilink.api.model.Project;
import com.unilink.api.model.User;
import com.unilink.api.repository.ProjectRepository;
import com.unilink.api.repository.UserRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public User getUserById(UUID id) {
        return this.userRepository.findById(id).orElse(null);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Transactional
    public User createUser(UserRequestDTO userRequest) {
        User newUser = new User();
        newUser.setName(userRequest.name());
        newUser.setEmail(userRequest.email());

        System.out.println("request password" + userRequest.password());

        String criptoPassword = passwordEncoder.encode(userRequest.password());
        newUser.setPassword(criptoPassword);

        System.out.println("cripto password" + criptoPassword);

        newUser.setRole(userRequest.role());
        return this.userRepository.save(newUser);
    }

    @Transactional
    public void deleteUser(UUID id) {
        User user = this.getUserById(id);

        List<Project> projects = this.projectRepository.findByOwnerId(id);
        for (Project project : projects) {
            project.setOwner(null);
            this.projectRepository.save(project);
        }
        
        this.userRepository.delete(user);
    }

    @Transactional
    public User updateUser(UUID id, UserRequestDTO updatedUser) {
        User existingUser = this.getUserById(id);

        if (updatedUser.name() != null) existingUser.setName(updatedUser.name());
        if (updatedUser.email() != null) existingUser.setEmail(updatedUser.email());
        if (updatedUser.password() != null) existingUser.setPassword(updatedUser.password());
        if (updatedUser.role() != null) existingUser.setRole(updatedUser.role());

        return this.userRepository.save(existingUser);
    }
}
