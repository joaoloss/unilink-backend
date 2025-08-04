package com.unilink.api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.unilink.api.exceptions.NotFoundException;
import com.unilink.api.model.Tag;
import com.unilink.api.repository.TagRepository;

@Service
public class TagService {
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    // Add service methods here
    public List<Tag> getAllTags() {
        return this.tagRepository.findAll();
    }

    public Tag getTagById(UUID id) {
        return this.tagRepository.findById(id).orElseThrow(() -> new NotFoundException("Tag not found with id: " + id));
    }

    public Tag createTag(Tag tag) {
        return this.tagRepository.save(tag);
    }

    public Tag updateTagName(UUID id, String name) {
        Tag tag = this.getTagById(id);
        tag.setName(name);
        return this.tagRepository.save(tag);
    }

    public Tag updateTagColor(UUID id, String colorHex) {
        Tag tag = this.getTagById(id);
        tag.setColorHex(colorHex);
        return this.tagRepository.save(tag);
    }

    public void deleteTag(UUID id) {
        Tag tag = this.getTagById(id);
        this.tagRepository.delete(tag);
    }
}