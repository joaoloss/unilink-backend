package com.unilink.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.unilink.api.model.User;
import com.unilink.api.security.JwtUtil;
import com.unilink.api.dtos.UserRequestDTO;
import com.unilink.api.exception.NotFoundException;
import com.unilink.api.service.UserService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequestDTO userRequestDTO) {
        System.out.println("request password" + userRequestDTO);
        User user = userService.createUser(userRequestDTO);

        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequestDTO userRequestDTO) {
        User user = userService.getUserByEmail(userRequestDTO.email());
        if(user == null) {
            throw new NotFoundException("User not found by email");
        }
        if(passwordEncoder.matches(userRequestDTO.password(), user.getPassword())) {
            String token = JwtUtil.generateToken(user.getId().toString());
            return ResponseEntity.ok(Map.of("token", token));
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }
}
