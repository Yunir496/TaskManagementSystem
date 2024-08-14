package com.example.taskmanagementsystem.dto.task;

import com.example.taskmanagementsystem.entity.enums.TaskPriority;
import com.example.taskmanagementsystem.entity.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для фильтрации задач
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskFilterDto {
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private Long creatorId;
    private Long executorId;
    private Integer page;
    private Integer size;
}