package com.unilink.api.domain.admin;

import java.util.UUID;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "admin")
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
   
    @Id
    @GeneratedValue
    @Setter(lombok.AccessLevel.NONE)
    private UUID id;
    
    private String name;
    private String email;
    private String password;
}
