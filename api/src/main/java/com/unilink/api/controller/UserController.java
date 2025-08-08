package com.unilink.api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unilink.api.dtos.UserRequestDTO;
import com.unilink.api.exception.InvalidFieldException;
import com.unilink.api.model.User;
import com.unilink.api.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(this.userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(this.userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserRequestDTO userRequest) {
        if(!userRequest.isValidForCreation()) {
            throw new InvalidFieldException("Invalid user data provided for creation.");
        }
        
        return ResponseEntity.ok(this.userService.createUser(userRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody UserRequestDTO updatedUser) {
        return ResponseEntity.ok(this.userService.updateUser(id, updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        this.userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
