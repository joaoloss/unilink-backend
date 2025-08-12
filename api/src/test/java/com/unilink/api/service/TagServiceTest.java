package com.unilink.api.service;

import com.unilink.api.dtos.TagRequestDTO;
import com.unilink.api.exception.NotFoundException;
import com.unilink.api.model.Project;
import com.unilink.api.model.Tag;
import com.unilink.api.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TagServiceTest {

    private TagRepository tagRepository;
    private TagService tagService;

    @BeforeEach
    void setUp() {
        tagRepository = mock(TagRepository.class);
        tagService = new TagService(tagRepository);
    }

    @Test
    @DisplayName("Should throw NotFoundException")
    void testGetTagByIdNotFound() {
        UUID id = UUID.randomUUID();
        when(tagRepository.findById(id)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> tagService.getTagById(id));
        assertTrue(ex.getMessage().contains(id.toString()));
    }

    @Test
    @DisplayName("Should update the name and the color of the tag")
    void testUpdateTagSuccess() {
        UUID id = UUID.randomUUID();
        Tag originalTag = new Tag();
        originalTag.setName("OldName");
        originalTag.setColorHex("#000000");

        TagRequestDTO dto = mock(TagRequestDTO.class);
        when(dto.name()).thenReturn("NewName");
        when(dto.colorHex()).thenReturn("#123456");

        when(tagRepository.findById(id)).thenReturn(Optional.of(originalTag));
        when(tagRepository.save(any(Tag.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Tag result = tagService.updateTag(id, dto);

        assertEquals("NewName", result.getName());
        assertEquals("#123456", result.getColorHex());
        verify(tagRepository).save(originalTag);
    }

    @Test
    @DisplayName("Should update just the color of the tag")
    void testPartialUpdateTagSuccess() {
        UUID id = UUID.randomUUID();
        Tag originalTag = new Tag();
        originalTag.setName("OldName");
        originalTag.setColorHex("#000000");

        TagRequestDTO dto = mock(TagRequestDTO.class);
        when(dto.name()).thenReturn(null);
        when(dto.colorHex()).thenReturn("#654321");

        when(tagRepository.findById(id)).thenReturn(Optional.of(originalTag));
        when(tagRepository.save(any(Tag.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Tag result = tagService.updateTag(id, dto);

        assertEquals("OldName", result.getName());
        assertEquals("#654321", result.getColorHex());
        verify(tagRepository).save(originalTag);
    }

    @Test
    @DisplayName("Should remove the tag (also updating projects tags set)")
    void testDeleteTagSuccess() {
        UUID id = UUID.randomUUID();
        Tag tag = new Tag();
        Project project1 = mock(Project.class);
        Project project2 = mock(Project.class);

        Set<Project> projects = new HashSet<>(Arrays.asList(project1, project2));
        tag.setProjects(projects);

        when(tagRepository.findById(id)).thenReturn(Optional.of(tag));

        Set<Tag> tagsSet = new HashSet<>();
        tagsSet.add(tag);
        when(project1.getTags()).thenReturn(tagsSet);
        when(project2.getTags()).thenReturn(tagsSet);

        tagService.deleteTag(id);

        verify(project1).getTags();
        verify(project2).getTags();
        verify(tagRepository).delete(tag);
    }
}