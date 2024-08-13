package com.example.taskmanagementsystem.dao;

import com.example.taskmanagementsystem.entity.Comment;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {
    @NotNull
    Page<Comment> findAll(Specification<Comment> spec, @NotNull Pageable pageable);

    @NotNull
    List<Comment> findAll(Specification<Comment> spec);
}