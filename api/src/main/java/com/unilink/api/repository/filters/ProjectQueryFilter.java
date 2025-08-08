package com.unilink.api.repository.filters;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.unilink.api.model.Project;
import static com.unilink.api.repository.specifications.ProjectSpec.*;

public record ProjectQueryFilter(
    String name,
    Integer teamSizeGTE,
    Integer teamSizeLTE,
    UUID centerId,
    Boolean openForApplications,
    List<UUID> tagIds
) {

    public Specification<Project> toSpecification() {
        return containsName(name)
            .and(teamSizeGreaterThanOrEqualTo(teamSizeGTE))
            .and(teamSizeLessThanOrEqualTo(teamSizeLTE))
            .and(hasCenterId(centerId))
            .and(isOpenForApplications(openForApplications))
            .and(hasAllTags(tagIds));
    }
} 