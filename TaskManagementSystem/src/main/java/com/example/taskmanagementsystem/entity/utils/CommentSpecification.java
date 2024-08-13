package com.example.taskmanagementsystem.entity.utils;

import com.example.taskmanagementsystem.entity.Comment;
import com.example.taskmanagementsystem.dto.comment.CommentFilterDto;
import com.example.taskmanagementsystem.entity.Task;
import com.example.taskmanagementsystem.entity.User;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CommentSpecification {

    public static Specification<Comment> commentFilter(CommentFilterDto commentFilterDto) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (commentFilterDto.getDate() != null) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.function("DATE", Date.class, root.get("date")), commentFilterDto.getDate()));
            }
            if (Strings.isNotEmpty(commentFilterDto.getText())) {
                predicates.add(criteriaBuilder.like(root.get("text"), "%" + commentFilterDto.getText() + "%"));
            }
            if (Strings.isNotEmpty(commentFilterDto.getAuthorName())) {
                String[] parts = commentFilterDto.getAuthorName().split("\\s+");
                String firstName = parts[0];
                String secondName = (parts.length > 1) ? parts[1] : "";
                Join<Comment, User> authorJoin = root.join("author");
                predicates.add(criteriaBuilder.like(authorJoin.get("firstName"), "%" + firstName + "%"));
                predicates.add(criteriaBuilder.like(authorJoin.get("lastName"), "%" + secondName + "%"));
            }
            if (commentFilterDto.getTaskId() != null) {
                Join<Comment, Task> taskJoin = root.join("task");
                predicates.add(criteriaBuilder.equal(taskJoin.get("id"), commentFilterDto.getTaskId()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}