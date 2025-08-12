package com.unilink.api.service;

import com.unilink.api.dtos.ProjectRequestDTO;
import com.unilink.api.exception.NotFoundException;
import com.unilink.api.model.Center;
import com.unilink.api.model.Project;
import com.unilink.api.model.Tag;
import com.unilink.api.model.User;
import com.unilink.api.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
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
    @DisplayName("Should update a project successfully")
    void updateProjectSuccess() {
        UUID id = UUID.randomUUID();
        UUID centerId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        UUID idTagAdd1 = UUID.randomUUID(); UUID idTagAdd2 = UUID.randomUUID();
        UUID[] tagsAddIds = {idTagAdd1, idTagAdd2};

        UUID idTagRemove1 = UUID.randomUUID(); UUID idTagRemove2 = UUID.randomUUID();
        UUID[] tagsRemoveIds = {idTagRemove1, idTagRemove2};

        ProjectRequestDTO dto = mock(ProjectRequestDTO.class);
        when(dto.name()).thenReturn("Updated");
        when(dto.description()).thenReturn("UpdatedDesc");
        when(dto.openForApplications()).thenReturn(false);
        when(dto.imgUrl()).thenReturn("updatedImg");
        when(dto.teamSize()).thenReturn(10);
        when(dto.centerId()).thenReturn(centerId);
        when(dto.ownerId()).thenReturn(ownerId);
        when(dto.tagsToBeAdded()).thenReturn(tagsAddIds);
        when(dto.tagsToBeRemoved()).thenReturn(tagsRemoveIds);

        Project original = mock();
        original.setOpenForApplications(true);
        when(projectRepository.findById(id)).thenReturn(Optional.of(original));
        when(centerService.getCenterById(centerId)).thenReturn(new Center());
        when(userService.getUserById(ownerId)).thenReturn(new User());
        
        Tag tagAdd1 = new Tag(); Tag tagAdd2 = new Tag();
        Tag tagRemove1 = new Tag(); Tag tagRemove2 = new Tag();
        when(tagService.getTagById(idTagAdd1)).thenReturn(tagAdd1); when(tagService.getTagById(idTagAdd2)).thenReturn(tagAdd2); 
        when(tagService.getTagById(idTagRemove1)).thenReturn(tagRemove1); when(tagService.getTagById(idTagRemove2)).thenReturn(tagRemove2);
        doNothing().when(original).addTag(any());

        Project saved = new Project();
        when(projectRepository.save(any(Project.class))).thenReturn(saved);

        Project result = projectService.updateProject(id, dto);

        assertEquals(saved, result);
        verify(projectRepository).save(original);
        verify(centerService).getCenterById(centerId);
        verify(userService).getUserById(ownerId);
        verify(tagService, times(4)).getTagById(any());
    }

    @Test
    @DisplayName("Should remove a tag from project successfully")
    void removeTagFromProjectSuccess() {
        UUID projectId = UUID.randomUUID();
        Tag tag = mock();
        Project project = mock();
        Set<Tag> tags = new HashSet<>();
        tags.add(tag);
        project.setTags(tags);
        Set<Project> projects = new HashSet<>();
        projects.add(project);
        tag.setProjects(projects);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(projectRepository.save(project)).thenReturn(project);

        projectService.removeTagFromProject(projectId, tag);

        assertFalse(project.getTags().contains(tag));
        assertFalse(tag.getProjects().contains(project));
        verify(projectRepository).save(project);
    }
}