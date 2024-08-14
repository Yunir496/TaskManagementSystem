package com.example.taskmanagementsystem.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * DTO для установки исполнителя задачи.
 */
@Data
@AllArgsConstructor
public class SetExecutorTaskDto {
    @NotNull
    private Long id;
    @NotNull
    private Long executorId;
}