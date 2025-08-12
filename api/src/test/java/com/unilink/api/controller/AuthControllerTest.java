package com.unilink.api.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.unilink.api.dtos.UserRequestDTO;
import com.unilink.api.exception.LoginFailureException;
import com.unilink.api.model.User;
import com.unilink.api.service.UserService;

public class AuthControllerTest {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should login successfully")
    void testLoginSuccess() {
        String email = "joao@dev.com";
        User user = spy(new User(UUID.randomUUID(), "João", email, passwordEncoder.encode("joao123"), null));
        UserRequestDTO dto = new UserRequestDTO("João", email, "joao123", null);
        
        when(userService.getUserByEmail(email)).thenReturn(user);

        authController.login(dto);

        verify(user).getId();
        
    }
    
    @Test
    @DisplayName("Should throw LoginFailureException due to different passwords")
    void testLoginFailure() {
        String email = "joao@dev.com";
        User user = new User(UUID.randomUUID(), "João", email, passwordEncoder.encode("joao1234"), null);
        UserRequestDTO dto = new UserRequestDTO("João", email, "joao123", null);
        
        when(userService.getUserByEmail(email)).thenReturn(user);

        assertThrows(LoginFailureException.class, () -> authController.login(dto));
    }
}
