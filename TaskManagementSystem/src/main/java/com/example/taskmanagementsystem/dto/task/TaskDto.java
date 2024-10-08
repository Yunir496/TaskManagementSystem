package com.example.taskmanagementsystem.dto.task;

import com.example.taskmanagementsystem.dto.comment.CommentDto;
import com.example.taskmanagementsystem.entity.Task;
import com.example.taskmanagementsystem.entity.enums.TaskPriority;
import com.example.taskmanagementsystem.entity.enums.TaskStatus;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO для задач.
 */
@Data
public class TaskDto {
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private String creatorName;
    private String executorName;
    private List<CommentDto> comments = new ArrayList<>();

    /**
     * Преобразует объект TaskDto в объект Task.
     *
     * @return Task объект задачи
     */
    public Task toTask() {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(status);
        task.setPriority(priority);
        return task;
    }

    /**
     * Преобразует объект Task в объект TaskDto.
     * @param task объект задачи
     *
     * @return TaskDto объект задачи
     */
    public static TaskDto fromTask(Task task) {
        TaskDto dto = new TaskDto();
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setPriority(task.getPriority());
        if (task.getCreator() != null) {
            dto.setCreatorName(task.getCreator().getFirstName() + " " + task.getCreator().getLastName());
        }
        if (task.getExecutor() != null) {
            dto.setExecutorName(task.getExecutor().getFirstName() + " " + task.getExecutor().getLastName());
        }
        if (task.getComments() != null) {
            dto.setComments(task.getComments().stream().map(CommentDto::fromComment).toList());
        }
        return dto;
    }
}