package com.unilink.api.service;

import com.unilink.api.dtos.ProjectRequestDTO;
import com.unilink.api.exception.NotFoundException;
import com.unilink.api.exception.AccessDeniedException;
import com.unilink.api.model.Project;
import com.unilink.api.model.Tag;
import com.unilink.api.model.User;
import com.unilink.api.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private CenterService centerService;
    @Mock
    private TagService tagService;
    @Mock
    private UserService userService;

    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should throw NotFoundException")
    void getProjectByIdNotFoundException() {
        UUID id = UUID.randomUUID();
        when(projectRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> projectService.getProjectById(id));
    }

    @Test
    @DisplayName("Should not update project due to because the user isn't the project owner")
    void updateProjectAccessDeniedException() {
        UUID projectId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        UUID currentUserId = UUID.randomUUID();

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(currentUserId.toString());
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.isAuthenticated()).thenReturn(true);

        ProjectRequestDTO dto = mock(ProjectRequestDTO.class);
        when(dto.name()).thenReturn("NewName");

        User owner = mock(User.class);
        when(owner.getId()).thenReturn(ownerId);

        Project original = spy(new Project(projectId, "OldName", null, false, null, 0, owner, null, null));

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(original));
        when(projectRepository.save(any(Project.class))).thenReturn(original);

        assertThrows(AccessDeniedException.class, () -> projectService.updateProject(projectId, dto));
    }

    @Test
    @DisplayName("Should update a project successfully")
    void updateProjectSuccess() {
        UUID projectId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        UUID idTagAdd1 = UUID.randomUUID(); UUID idTagAdd2 = UUID.randomUUID();
        UUID[] tagsAddIds = {idTagAdd1, idTagAdd2};

        UUID idTagRemove1 = UUID.randomUUID(); UUID idTagRemove2 = UUID.randomUUID();
        UUID[] tagsRemoveIds = {idTagRemove1, idTagRemove2};

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(ownerId.toString());
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.isAuthenticated()).thenReturn(true);

        ProjectRequestDTO dto = mock(ProjectRequestDTO.class);
        when(dto.name()).thenReturn("NewName");
        when(dto.description()).thenReturn("NewDesc");
        when(dto.tagsToBeAdded()).thenReturn(tagsAddIds);
        when(dto.tagsToBeRemoved()).thenReturn(tagsRemoveIds);

        User owner = mock(User.class);
        when(owner.getId()).thenReturn(ownerId);

        Project original = spy(new Project(projectId, "OldName", "OldDesc", false, null, 0, owner, null, new HashSet<Tag>()));
        when(original.isOpenForApplications()).thenReturn(true);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(original));
        
        Tag tagAdd1 = new Tag(); Tag tagAdd2 = new Tag();
        Tag tagRemove1 = new Tag(); Tag tagRemove2 = new Tag();
        when(tagService.getTagById(idTagAdd1)).thenReturn(tagAdd1); when(tagService.getTagById(idTagAdd2)).thenReturn(tagAdd2); 
        when(tagService.getTagById(idTagRemove1)).thenReturn(tagRemove1); when(tagService.getTagById(idTagRemove2)).thenReturn(tagRemove2);
        doNothing().when(original).addTag(any());
        doNothing().when(original).removeTag(any());

        when(projectRepository.save(any(Project.class))).thenReturn(original);

        Project result = projectService.updateProject(projectId, dto);

        assertEquals(result.getName(), dto.name());
        assertEquals(result.getDescription(), dto.description());
        verify(projectRepository).save(original);
        verify(tagService, times(4)).getTagById(any());
    }
}