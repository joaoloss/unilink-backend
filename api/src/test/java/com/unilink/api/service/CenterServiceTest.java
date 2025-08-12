package com.unilink.api.service;

import com.unilink.api.exception.NotFoundException;
import com.unilink.api.model.Center;
import com.unilink.api.model.Project;
import com.unilink.api.repository.CenterRepository;
import com.unilink.api.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CenterServiceTest {

    @Mock
    private CenterRepository centerRepository;
    
    @Mock
    private ProjectRepository projectRepository;
    
    @InjectMocks
    @Autowired
    private CenterService centerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should delete a center and set null to center field of corresponding projects")
    void deleteCenterSuccess() {
        UUID centerId = UUID.randomUUID();
        Center center = new Center(centerId, null, null);

        Project project1 = new Project(UUID.randomUUID(), null, null, false, null, 0, null, center, null);
        Project project2 = new Project(UUID.randomUUID(), null, null, false, null, 0, null, center, null);

        List<Project> projects = Arrays.asList(project1, project2);

        when(centerRepository.findById(centerId)).thenReturn(Optional.of(center));
        when(projectRepository.findByCenterId(centerId)).thenReturn(projects);

        centerService.deleteCenter(centerId);

        // Verify projects are unlinked and saved
        verify(projectRepository, times(2)).save(any(Project.class));
        assertNull(project1.getCenter());
        assertNull(project2.getCenter());

        // Verify center is deleted
        verify(centerRepository).delete(center);
    }

    @Test
    @DisplayName("Should throw NotFoundException")
    void deleteCenterNotFoundException() {
        UUID centerId = UUID.randomUUID();
        when(centerRepository.findById(centerId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> centerService.deleteCenter(centerId));
        verify(centerRepository, never()).delete(any());
        verify(projectRepository, never()).findByCenterId(any());
    }

    @Test
    @DisplayName("Should remove center with no projects associated")
    void deleteCenterWithAssociatedProjects() {
        UUID centerId = UUID.randomUUID();
        Center center = new Center(centerId, null, null);

        when(centerRepository.findById(centerId)).thenReturn(Optional.of(center));
        when(projectRepository.findByCenterId(centerId)).thenReturn(Collections.emptyList());

        centerService.deleteCenter(centerId);

        verify(projectRepository, never()).save(any());
        verify(centerRepository).delete(center);
    }
}