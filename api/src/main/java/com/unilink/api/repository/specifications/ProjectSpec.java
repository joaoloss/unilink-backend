package com.unilink.api.repository.specifications;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.jpa.domain.Specification;

import com.unilink.api.model.Project;
import com.unilink.api.model.Tag;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public class ProjectSpec {
    public static Specification<Project> containsName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (ObjectUtils.isEmpty(name)) {
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Project> hasCenterId(UUID centerId) {
        return (root, query, criteriaBuilder) -> {
            if (ObjectUtils.isEmpty(centerId)) {
                return null;
            }
            return criteriaBuilder.equal(root.get("center").get("id"), centerId);
        };
    }

    public static Specification<Project> hasAllTags(List<UUID> tagIds) {
        return (root, query, criteriaBuilder) -> {
            if (ObjectUtils.isEmpty(tagIds)) {
                return null;
            }

            // subquery que conta quantas tags do projeto estão na lista
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Project> subRoot = subquery.from(Project.class);
            Join<Project, Tag> join = subRoot.join("tags");
            subquery.select(criteriaBuilder.countDistinct(join.get("id")))
                .where(criteriaBuilder.equal(subRoot.get("id"), root.get("id")),
                    join.get("id").in(tagIds));

            // exige que o número de tags relacionadas seja igual ao número de tags filtradas
            return criteriaBuilder.equal(subquery, (long) tagIds.size());
        };
    }

    public static Specification<Project> isOpenForApplications(Boolean openForApplications) {
        return (root, query, criteriaBuilder) -> {
            if (ObjectUtils.isEmpty(openForApplications)) {
                return null;
            }
            return criteriaBuilder.equal(root.get("openForApplications"), openForApplications);
        };
    }

    public static Specification<Project> teamSizeLessThanOrEqualTo(Integer teamSize) {
        return (root, query, criteriaBuilder) -> {
            if (ObjectUtils.isEmpty(teamSize)) {
                return null;
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("teamSize"), teamSize);
        };
    }

    public static Specification<Project> teamSizeGreaterThanOrEqualTo(Integer teamSize) {
        return (root, query, criteriaBuilder) -> {
            if (ObjectUtils.isEmpty(teamSize)) {
                return null;
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("teamSize"), teamSize);
        };
    }



}
