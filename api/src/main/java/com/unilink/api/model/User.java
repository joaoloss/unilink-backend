package com.unilink.api.model;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(lombok.AccessLevel.NONE)
    private UUID id;

    private String name;
    private String email;
    private String password;
    private UserRole role;
    private Set<UUID> projectIds;

    /**
     * Adds a project ID to the list of projects owned by the user.
     * If the project list is not initialized, it creates a new list.
     *
     * @param projectId the UUID of the project to add to the user's owned projects
     */
    public void addOwnedProject(UUID projectId) {
        if (this.projectIds == null) {
            this.projectIds = new HashSet<>(); // Initialize the set if it's null
        }
        
        // Add the project ID to the list
        this.projectIds.add(projectId);
    }

    /**
     * Removes the specified project ID from the list of projects owned by the user.
     *
     * @param projectId the UUID of the project to be removed from the user's owned projects
     */
    public void removeOwnedProject(UUID projectId) {
        if (this.projectIds != null) {
            this.projectIds.remove(projectId);
        }
    }
}
