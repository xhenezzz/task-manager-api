package aidyn.kelbetov.utils;

import aidyn.kelbetov.model.Priority;
import aidyn.kelbetov.model.Status;
import aidyn.kelbetov.model.Task;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskSpecification {
    public static Specification<Task> byFilters(Status status, Priority priority, Date dueDate, Long userId){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(userId != null){
                predicates.add(criteriaBuilder.equal(root.get("userId"), userId));
            }

            if(status != null){
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if(priority != null){
                predicates.add(criteriaBuilder.equal(root.get("priority"), priority));
            }

            if(dueDate != null){
                predicates.add(criteriaBuilder.equal(root.get("dueDate"), dueDate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
