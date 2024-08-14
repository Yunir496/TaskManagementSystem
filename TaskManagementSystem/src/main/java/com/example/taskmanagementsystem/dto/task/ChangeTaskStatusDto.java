package com.example.taskmanagementsystem.dto.task;

import com.example.taskmanagementsystem.entity.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * DTO для изменения статуса задачи.
 */
@Data
@AllArgsConstructor
public class ChangeTaskStatusDto {
    @NotNull
    private Long taskId;
    @NotNull
    private TaskStatus newStatus;
}