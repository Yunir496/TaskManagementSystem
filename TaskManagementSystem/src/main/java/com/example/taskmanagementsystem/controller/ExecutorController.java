package com.example.taskmanagementsystem.controller;

import com.example.taskmanagementsystem.dto.task.ChangeTaskStatusDto;
import com.example.taskmanagementsystem.dto.task.TaskDto;
import com.example.taskmanagementsystem.dto.task.TaskFilterDto;
import com.example.taskmanagementsystem.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/executors/tasks/")
public class ExecutorController {
    @Autowired
    private TaskService taskService;

    @PostMapping("status")
    public ResponseEntity<TaskDto> changeTaskStatus(@RequestBody @Validated ChangeTaskStatusDto taskDto) {
        return ResponseEntity.ok(taskService.changeStatus(taskDto));
    }

    @PostMapping("/all")
    public ResponseEntity<List<TaskDto>> getAllTasks(@RequestBody(required = false) TaskFilterDto taskFilterDto) {
        return ResponseEntity.ok(taskService.findAll(taskFilterDto));
    }
}