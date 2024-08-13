package com.example.taskmanagementsystem.service;

import com.example.taskmanagementsystem.dto.task.ChangeTaskStatusDto;
import com.example.taskmanagementsystem.dto.task.CreateTaskDto;
import com.example.taskmanagementsystem.dto.task.TaskFilterDto;
import com.example.taskmanagementsystem.dto.user.SetExecutorTaskDto;
import com.example.taskmanagementsystem.dto.task.TaskDto;
import org.springframework.lang.Nullable;

import java.util.List;


public interface TaskService {

    TaskDto createOrUpdate(CreateTaskDto taskDto);

    List<TaskDto> findAll(@Nullable TaskFilterDto taskFilterDto);

    TaskDto findById(Long id);

    void deleteById(Long id);

    TaskDto setExecutor(SetExecutorTaskDto taskDto);

    TaskDto changeStatus(ChangeTaskStatusDto taskStatusDto);
}