package com.unilink.api.domain.project;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "project")
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    
    @Id
    @GeneratedValue
    @Setter(lombok.AccessLevel.NONE)
    private UUID id;
    private String name;
    private String description;
    private String imageUrl;
    private String leaderEmail;
    private boolean hiring;
    private String departament;
    private String university;
    private Date createdAt;
}
