package com.unilink.api.model;

import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue
    @Setter(lombok.AccessLevel.NONE)
    UUID id;

    String colorHex;
    String name;

    @ManyToMany(mappedBy = "tags")
    Set<Project> projects;

    public void addProject(Project project) {
        projects.add(project);
        project.getTags().add(this);
    }
    public void removeProject(Project project) {
        projects.remove(project);
        project.getTags().remove(this);
    }
}
