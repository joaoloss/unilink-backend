package com.unilink.api.model;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.ForeignKey;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "projects")
public class Project {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(lombok.AccessLevel.NONE)
    private UUID id;
    
    private String name;
    private String description;
    private String imgUrl;
    private boolean openForApplications;
    private int teamSize;

    private UUID ownerId;
    private UUID centerId;
    private List<UUID> tagsIds;
}
