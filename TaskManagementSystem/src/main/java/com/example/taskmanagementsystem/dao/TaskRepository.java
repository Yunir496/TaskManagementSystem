package com.example.taskmanagementsystem.dao;

import com.example.taskmanagementsystem.entity.Task;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    @NotNull
    Page<Task> findAll(Specification<Task> spec, @NotNull Pageable pageable);

    @NotNull
    List<Task> findAll(Specification<Task> spec);
}