package com.khan.job_quest.jobs.spec;

import com.khan.job_quest.jobs.entity.Job;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.Objects;

public class JobSpecification {
    @SafeVarargs
    public static <T> Specification<T> safeAnd(Specification<T>... specs) {
        return Arrays.stream(specs)
                .filter(Objects::nonNull)
                .reduce(Specification.allOf(), Specification::and);
    }

    public static Specification<Job> hasKeyword(String keyword) {
        return ((root, query, criteriaBuilder) -> {
           if (keyword == null || keyword.isEmpty()) {
               return null;
           }

           String likePattern = "%" + keyword.toLowerCase() + "%";
           return criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), likePattern),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), likePattern),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("companyName")), likePattern)
           );
        });
    }

    public static Specification<Job> hasLocation(String location) {
        return ((root, query, criteriaBuilder) -> {
            if (location == null || location.isEmpty()) {
                return null;
            }

            String likePattern = "%" + location.toLowerCase() + "%";
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("location")), likePattern);
        });
    }

    public static Specification<Job> withinSalaryRange(Integer minSalary, Integer maxSalary) {
        return ((root, query, criteriaBuilder) -> {
            if (minSalary == null && maxSalary == null) {
                return null;
            } else if (minSalary != null && maxSalary != null) {
                return criteriaBuilder.between(root.get("salary"), minSalary.doubleValue(), maxSalary.doubleValue());
            } else if (minSalary != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("salary"), minSalary.doubleValue());
            } else {
                return criteriaBuilder.lessThanOrEqualTo(root.get("salary"), maxSalary.doubleValue());
            }
        });
    }
}
