package com.unilink.api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unilink.api.model.Tag;
import com.unilink.api.service.TagService;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public List<Tag> getAllTags() {
        return this.tagService.getAllTags();
    }

    @GetMapping("/{id}")
    public Tag getTagById(@PathVariable UUID id) {
        return this.tagService.getTagById(id);
    }

    @PostMapping
    public Tag createTag(@RequestBody Tag tag) {
        return this.tagService.createTag(tag);
    }

    @DeleteMapping("/{id}")
    public void deleteTag(@PathVariable UUID id) {
        this.tagService.deleteTag(id);
    }

    @PostMapping("/{id}/name")
    public Tag updateTagName(@PathVariable UUID id, @RequestBody String name) {
        return this.tagService.updateTagName(id, name);
    }
    
    @PostMapping("/{id}/color")
    public Tag updateTagColor(@PathVariable UUID id, @RequestBody String colorHex) {
        return this.tagService.updateTagColor(id, colorHex);
    }
}
