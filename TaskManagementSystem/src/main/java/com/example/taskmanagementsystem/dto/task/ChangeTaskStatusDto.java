package com.example.taskmanagementsystem.dto.task;


import com.example.taskmanagementsystem.entity.enums.TaskStatus;
import lombok.Data;


@Data
public class ChangeTaskStatusDto {
    private Long taskId;
    private TaskStatus newStatus;
}