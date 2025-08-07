package com.unilink.api.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
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
@Table(name = "centers")
public class Center {
    
    @Id
    @GeneratedValue
    @Setter(lombok.AccessLevel.NONE)
    private UUID id;
    
    private String name;
    private String centerUrl;

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof Center)) return false;
        Center other = (Center) obj;
        return this.id != null && this.id.equals(other.id);
    }
}
