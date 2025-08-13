package com.unilink.api.model;

import java.util.Set;
import java.util.UUID;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
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

    @Column(unique = true)
    String name;

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    Set<Project> projects;

    public void addProject(Project project) {
        this.projects.add(project);
        project.getTags().add(this);
    }
    public void removeProject(Project project) {
        this.projects.remove(project);
        project.getTags().remove(this);
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof Tag)) return false;
        Tag other = (Tag) obj;
        return this.id != null && this.id.equals(other.id);
    }
}
