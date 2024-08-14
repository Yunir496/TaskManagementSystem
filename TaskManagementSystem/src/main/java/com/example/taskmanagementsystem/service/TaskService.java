package com.example.taskmanagementsystem.service;

import com.example.taskmanagementsystem.dto.task.ChangeTaskStatusDto;
import com.example.taskmanagementsystem.dto.task.CreateTaskDto;
import com.example.taskmanagementsystem.dto.task.TaskFilterDto;
import com.example.taskmanagementsystem.dto.user.SetExecutorTaskDto;
import com.example.taskmanagementsystem.dto.task.TaskDto;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * Сервисный слой для управления заданиями.
 */
public interface TaskService {
    /**
     * Создать новое задание или обновить существующее.
     *
     * @param taskDto данные для создания или обновления задания
     * @return созданное или обновленное задание
     */
    TaskDto createOrUpdate(CreateTaskDto taskDto);

    /**
     * Получить список всех заданий.
     *
     * @param taskFilterDto данные для создания спецификации и пагинации
     * @return список всех заданий
     */
    List<TaskDto> findAll(@Nullable TaskFilterDto taskFilterDto);

    /**
     * Найти задание по его идентификатору.
     *
     * @param id идентификатор задания
     * @return найденное задание или null, если не найдено
     */
    TaskDto findById(Long id);

    /**
     * Удалить задание по его идентификатору.
     *
     * @param id идентификатор задания
     */
    void deleteById(Long id);

    /**
     * Назначить исполнителя для задания.
     * @param taskDto данные для установки исполнителя задания
     * @return задание с назначенным исполнителем
     */
    TaskDto setExecutor(SetExecutorTaskDto taskDto);

    /**
     * Изменить статус задачи
     *
     * @param taskStatusDto данные для изменения статуса задачи
     * @return задание с измененным статусом
     */
    TaskDto changeStatus(ChangeTaskStatusDto taskStatusDto);
}