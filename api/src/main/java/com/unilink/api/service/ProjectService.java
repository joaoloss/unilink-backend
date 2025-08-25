package com.unilink.api.service;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unilink.api.dtos.ProjectRequestDTO;
import com.unilink.api.exception.AccessDeniedException;
import com.unilink.api.exception.NotFoundException;
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
    private final R2StorageService r2StorageService;

    public List<Project> getAllProjects(Specification<Project> filterSpecification) {
        return this.projectRepository.findAll(filterSpecification);
    }

    public Project getProjectById(UUID id) {
        return this.projectRepository.findById(id).orElseThrow(() -> new NotFoundException("Project not found with id: " + id));
    }

    @Transactional
    public Project createProject(ProjectRequestDTO project) {
        Project newProject = new Project();
        newProject.setName(project.name());
        newProject.setDescription(project.description());
        newProject.setOpenForApplications(project.openForApplications());
        newProject.setTeamSize(project.teamSize());
        if(project.centerId() != null) newProject.setCenter(this.centerService.getCenterById(project.centerId()));
        if(project.ownerId() != null) newProject.setOwner(this.userService.getUserById(project.ownerId()));
        newProject.setTags(new HashSet<>());

        // Processar imagem base64 se fornecida (prioridade sobre imgUrl)
        if (project.hasImageData()) {
            String imageUrl = r2StorageService.uploadFromUri(
                project.imageBase64(),
                project.imageContentType()
            );
            newProject.setImgUrl(imageUrl);
        } else if (project.imgUrl() != null) {
            // Usar imgUrl apenas se não houver imageBase64
            newProject.setImgUrl(project.imgUrl());
        }

        if(project.tagsToBeAdded() != null) {
            for (UUID tagId : project.tagsToBeAdded()) {
                Tag tag = this.tagService.getTagById(tagId);
                newProject.addTag(tag);
            }
        }

        return this.projectRepository.save(newProject);
    }

    private boolean hasUpdateAccess(Project project) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = null;
        boolean isSuperAdmin = false;

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                currentUserId = ((UserDetails) principal).getUsername();
                for (GrantedAuthority auth : ((UserDetails) principal).getAuthorities()) {
                    if ("ROLE_SUPER_ADMIN".equals(auth.getAuthority())) {
                        isSuperAdmin = true;
                        break;
                    }
                }
            } else if (principal instanceof String) {
                currentUserId = (String) principal;
            }
        } else return false;

        return isSuperAdmin || (project.getOwner() != null && project.getOwner().getId().toString().equals(currentUserId));
    }
    
    @Transactional
    public Project updateProject(UUID id, ProjectRequestDTO updatedProject) {
        Project originalProject = this.getProjectById(id);

        if(!this.hasUpdateAccess(originalProject)) throw new AccessDeniedException();
        
        if(updatedProject.name() != null) originalProject.setName(updatedProject.name());
        if(updatedProject.description() != null) originalProject.setDescription(updatedProject.description());
        if(updatedProject.openForApplications() != originalProject.isOpenForApplications()) {
            originalProject.setOpenForApplications(updatedProject.openForApplications());
        }
        if(updatedProject.teamSize() > 0) originalProject.setTeamSize(updatedProject.teamSize());

        // Processar imagem base64 se fornecida (prioridade sobre imgUrl)
        if (updatedProject.hasImageData()) {
            // Remove a imagem anterior se existir
            if (originalProject.getImgUrl() != null) {
                r2StorageService.deleteByUrl(originalProject.getImgUrl());
            }
            
            String imageUrl = r2StorageService.uploadFromUri(
                updatedProject.imageBase64(),
                updatedProject.imageContentType()
            );
            originalProject.setImgUrl(imageUrl);
        } else if (updatedProject.imgUrl() != null) {
            // Usar imgUrl apenas se não houver imageBase64
            originalProject.setImgUrl(updatedProject.imgUrl());
        }

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

    @Transactional
    public void deleteProject(UUID id) {
        Project project = this.getProjectById(id);
        
        // Remove a imagem do projeto se existir
        if (project.getImgUrl() != null) {
            r2StorageService.deleteByUrl(project.getImgUrl());
        }
        
        this.projectRepository.delete(project);
    }
}