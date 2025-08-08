package com.unilink.api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.unilink.api.DTO.ProjectRequestDTO;
import com.unilink.api.exceptions.NotFoundException;
import com.unilink.api.model.Project;
import com.unilink.api.model.Tag;
import com.unilink.api.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final CenterService centerService;
    private final TagService tagService;
    private final UserService userService;

    public List<Project> getAllProjects() {
        return this.projectRepository.findAll();
    }

    public Project getProjectById(UUID id) {
        return this.projectRepository.findById(id).orElseThrow(() -> new NotFoundException("Project not found with id: " + id));
    }

    public Project createProject(ProjectRequestDTO project) {
        Project newProject = new Project();
        newProject.setName(project.name());
        newProject.setDescription(project.description());
        newProject.setOpenForApplications(project.openForApplications());
        newProject.setImgUrl(project.imgUrl());
        newProject.setTeamSize(project.teamSize());
        if(project.centerId() != null) newProject.setCenter(this.centerService.getCenterById(project.centerId()));
        if(project.ownerId() != null) newProject.setOwner(this.userService.getUserById(project.ownerId()));

        if(project.tagsToBeAdded() != null) {
            for (UUID tagId : project.tagsToBeAdded()) {
                Tag tag = this.tagService.getTagById(tagId);
                newProject.addTag(tag);
            }
        }

        return this.projectRepository.save(newProject);
    }
    
    public Project updateProject(UUID id, ProjectRequestDTO updatedProject) {
        Project originalProject = this.getProjectById(id);
        
        if(updatedProject.name() != null) originalProject.setName(updatedProject.name());
        if(updatedProject.description() != null) originalProject.setDescription(updatedProject.description());
        if(updatedProject.openForApplications() != originalProject.isOpenForApplications()) {
            originalProject.setOpenForApplications(updatedProject.openForApplications());
        }
        if(updatedProject.imgUrl() != null) originalProject.setImgUrl(updatedProject.imgUrl());
        if(updatedProject.teamSize() > 0) originalProject.setTeamSize(updatedProject.teamSize());

        if(updatedProject.centerId() != null) originalProject.setCenter(this.centerService.getCenterById(updatedProject.centerId()));
        if(updatedProject.ownerId() != null) originalProject.setOwner(this.userService.getUserById(updatedProject.ownerId()));

        if(updatedProject.tagsToBeAdded() != null) {
            for (UUID tagId : updatedProject.tagsToBeAdded()) {
                Tag tag = this.tagService.getTagById(tagId);
                originalProject.addTag(tag);
            }
        }

        if(updatedProject.tagsToBeRemoved() != null) {
            for (UUID tagId : updatedProject.tagsToBeRemoved()) {
                Tag tag = this.tagService.getTagById(tagId);
                originalProject.removeTag(tag);
            }
        }

        return this.projectRepository.save(originalProject);
    } 

    public void addTagToProject(UUID projectId, Tag tag) {
        Project project = this.getProjectById(projectId);
        project.addTag(tag);
        this.projectRepository.save(project);
    }

    public void removeTagFromProject(UUID projectId, Tag tag) {
        Project project = this.getProjectById(projectId);
        project.getTags().remove(tag);
        tag.getProjects().remove(project);
        this.projectRepository.save(project);
    }

    public void deleteProject(UUID id) {
        Project project = this.getProjectById(id);
        this.projectRepository.delete(project);
    }
}
