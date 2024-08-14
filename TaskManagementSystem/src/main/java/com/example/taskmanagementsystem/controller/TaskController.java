package com.example.taskmanagementsystem.controller;

import com.example.taskmanagementsystem.dto.task.ChangeTaskStatusDto;
import com.example.taskmanagementsystem.dto.task.CreateTaskDto;
import com.example.taskmanagementsystem.dto.task.TaskFilterDto;
import com.example.taskmanagementsystem.dto.user.SetExecutorTaskDto;
import com.example.taskmanagementsystem.dto.task.TaskDto;
import com.example.taskmanagementsystem.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks/")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping("{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable @Validated @NotNull Long id) {
        return ResponseEntity.ok(taskService.findById(id));
    }

    @PostMapping("all")
    public ResponseEntity<List<TaskDto>> getAllTasks(@RequestBody(required = false) TaskFilterDto taskFilterDto) {
        return ResponseEntity.ok(taskService.findAll(taskFilterDto));
    }

    @PostMapping
    public ResponseEntity<TaskDto> createTask(@RequestBody @Validated CreateTaskDto taskDto) {
        return ResponseEntity.ok(taskService.createOrUpdate(taskDto));
    }

    @PutMapping
    public ResponseEntity<TaskDto> updateTask(@RequestBody @Validated CreateTaskDto taskDto) {
        return ResponseEntity.ok(taskService.createOrUpdate(taskDto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteTask(@PathVariable @Validated @NotNull Long id) {
        taskService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("status")
    public ResponseEntity<TaskDto> changeTaskStatus(@RequestBody @Validated ChangeTaskStatusDto taskDto) {
        return ResponseEntity.ok(taskService.changeStatus(taskDto));
    }

    @PostMapping("executors")
    public ResponseEntity<TaskDto> setTaskExecutors(@RequestBody @Validated SetExecutorTaskDto taskDto) {
        return ResponseEntity.ok(taskService.setExecutor(taskDto));
    }
}