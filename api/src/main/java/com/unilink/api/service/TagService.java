package com.unilink.api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.unilink.api.DTO.TagRequestDTO;
import com.unilink.api.exceptions.NotFoundException;
import com.unilink.api.model.Project;
import com.unilink.api.model.Tag;
import com.unilink.api.repository.TagRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public List<Tag> getAllTags() {
        return this.tagRepository.findAll();
    }

    public Tag getTagById(UUID id) {
        return this.tagRepository.findById(id).orElseThrow(() -> new NotFoundException("Tag not found with id: " + id));
    }

    public Tag createTag(TagRequestDTO tag) {
        Tag newTag = new Tag();
        newTag.setName(tag.name());
        newTag.setColorHex(tag.colorHex());
        
        return this.tagRepository.save(newTag);
    }

    public Tag updateTag(UUID id, TagRequestDTO updatedTag) {
        Tag originalTag = this.getTagById(id);

        if(updatedTag.name() != null) originalTag.setName(updatedTag.name());
        if(updatedTag.colorHex() != null) originalTag.setColorHex(updatedTag.colorHex());
    
        return this.tagRepository.save(originalTag);
    }

    public void deleteTag(UUID id) {
        Tag tag = this.getTagById(id);

        for(Project project : tag.getProjects()) {
            project.getTags().remove(tag);
        }

        this.tagRepository.delete(tag);
    }
}