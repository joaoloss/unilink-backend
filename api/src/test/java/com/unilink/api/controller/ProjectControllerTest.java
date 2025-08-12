package com.unilink.api.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.unilink.api.service.ProjectService;
import com.unilink.api.dtos.ProjectRequestDTO;
import com.unilink.api.exception.InvalidFieldException;

public class ProjectControllerTest {
    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectController projectController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should throw InvalidFieldException due to empty OwnerId")
    void testGetUserByEmailFound() {
        ProjectRequestDTO dto = new ProjectRequestDTO("TestName", "TestDesc", null, null, false, null, 0, null, null);
        assertThrows(InvalidFieldException.class, () -> projectController.createProject(dto));
    }    
}
