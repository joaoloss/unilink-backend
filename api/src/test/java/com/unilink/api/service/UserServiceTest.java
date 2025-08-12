package com.unilink.api.service;

import com.unilink.api.dtos.UserRequestDTO;
import com.unilink.api.model.Project;
import com.unilink.api.model.User;
import com.unilink.api.enums.UserRole;
import com.unilink.api.repository.ProjectRepository;
import com.unilink.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Spy
    private PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should found user by email successfully")
    void testGetUserByEmailFound() {
        String email = "test@example.com";
        User user = new User();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        User result = userService.getUserByEmail(email);

        assertEquals(user, result);
        verify(userRepository).findByEmail(email);
    }

    @Test
    @DisplayName("Should not found user by email, returning null")
    void testGetUserByEmailNotFound() {
        String email = "notfound@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        User result = userService.getUserByEmail(email);

        assertNull(result);
    }

    @Test
    @DisplayName("Should remove a user, updating the owned projects")
    void testDeleteUser() {
        UUID id = UUID.randomUUID();
        User user = new User();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        List<Project> projects = Arrays.asList(new Project(), new Project());
        when(projectRepository.findByOwnerId(id)).thenReturn(projects);

        userService.deleteUser(id);

        for (Project project : projects) {
            assertNull(project.getOwner());
        }
        verify(projectRepository, times(2)).save(any(Project.class));
        verify(userRepository).delete(user);
    }

    @Test
    @DisplayName("Should performe partial update")
    void testUpdateUser() {
        UUID id = UUID.randomUUID();
        User existingUser = new User();
        existingUser.setName("Old Name");
        existingUser.setEmail("old@example.com");
        existingUser.setPassword("oldPassword");
        existingUser.setRole(UserRole.PROJECT_ADMIN);

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));

        UserRequestDTO dto = mock(UserRequestDTO.class);
        when(dto.name()).thenReturn(null);
        when(dto.email()).thenReturn("new@example.com");
        when(dto.password()).thenReturn("newPassword");
        when(dto.role()).thenReturn(UserRole.SUPER_ADMIN);

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.updateUser(id, dto);

        assertEquals(result.getEmail(), "new@example.com");
        assertEquals("Old Name", result.getName());
        verify(userRepository).save(existingUser);
    }
}