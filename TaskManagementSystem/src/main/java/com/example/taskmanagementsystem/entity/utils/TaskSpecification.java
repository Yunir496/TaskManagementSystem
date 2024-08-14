package com.example.taskmanagementsystem.entity.utils;

import com.example.taskmanagementsystem.dto.task.TaskFilterDto;
import com.example.taskmanagementsystem.entity.Task;
import com.example.taskmanagementsystem.entity.User;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс спецификации задач.
 */
public class TaskSpecification {
    /**
     * Создает спецификацию запроса для фильтрации задач на основе заданных критериев.
     * @param taskFilterDto объект, содержащий критерии фильтрации задач
     *
     * @return спецификацию запроса для фильтрации задач
     */
    public static Specification<Task> taskFilter(TaskFilterDto taskFilterDto) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (Strings.isNotEmpty(taskFilterDto.getTitle())) {
                predicates.add(criteriaBuilder.like(root.get("title"), "%" + taskFilterDto.getTitle() + "%"));
            }
            if (Strings.isNotEmpty(taskFilterDto.getDescription())) {
                predicates.add(criteriaBuilder.like(root.get("description"), "%" + taskFilterDto.getDescription() + "%"));
            }
            if (taskFilterDto.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), taskFilterDto.getStatus()));
            }
            if (taskFilterDto.getPriority() != null) {
                predicates.add(criteriaBuilder.equal(root.get("priority"), taskFilterDto.getPriority()));
            }
            if (taskFilterDto.getCreatorId() != null) {
                Join<Task, User> creatorJoin = root.join("creator");
                predicates.add(criteriaBuilder.equal(creatorJoin.get("id"), taskFilterDto.getCreatorId()));
            }
            if (taskFilterDto.getExecutorId() != null) {
                Join<Task, User> executorJoin = root.join("executor");
                predicates.add(criteriaBuilder.equal(executorJoin.get("id"), taskFilterDto.getExecutorId()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

}