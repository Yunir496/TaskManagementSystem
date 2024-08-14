package com.example.taskmanagementsystem.dto.task;

import com.example.taskmanagementsystem.entity.Task;
import com.example.taskmanagementsystem.entity.enums.TaskPriority;
import com.example.taskmanagementsystem.entity.enums.TaskStatus;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * DTO для создания задач.
 */
@Data
public class CreateTaskDto {
    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotNull
    private TaskStatus status;
    @NotNull
    private TaskPriority priority;
    @NotNull
    private Long creatorId;
    @NotNull
    private Long executorId;

    /**
     * Преобразует объект CreateTaskDto в объект Task.
     *
     * @return Task объект задачи
     */
    public Task toTask() {
        Task task = new Task();
        task.setId(id != null ? id : null);
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(status);
        task.setPriority(priority);
        return task;
    }
}