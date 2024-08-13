package com.example.taskmanagementsystem.dto.task;

import com.example.taskmanagementsystem.entity.Task;

import com.example.taskmanagementsystem.entity.enums.TaskPriority;
import com.example.taskmanagementsystem.entity.enums.TaskStatus;
import lombok.Data;


@Data
public class CreateTaskDto {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private Long creatorId;
    private Long executorId;


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