package com.example.taskmanagementsystem.service;

import com.example.taskmanagementsystem.dto.comment.CommentDto;
import com.example.taskmanagementsystem.dto.comment.CommentFilterDto;
import com.example.taskmanagementsystem.dto.comment.CreateCommentDto;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * Сервисный слой предоставляет методы для управления комментариями.
 */
public interface CommentService {
    /**
     * Сохраняет новый комментарий.
     *
     * @param commentDto данные комментария для сохранения
     * @return сохраненный комментарий
     */
    CommentDto save(CreateCommentDto commentDto);

    /**
     * Получает все комментарии.
     * @param commentFilterDto информация о пагинации и фильтрации
     *
     * @return список всех комментариев
     */
    List<CommentDto> findAll(@Nullable CommentFilterDto commentFilterDto);

    /**
     * Получает комментарий по его идентификатору.
     * @param id уникальный идентификатор комментария
     *
     * @return найденный комментарий
     */
    CommentDto findById(Long id);

    /**
     * Удаляет комментарий по его идентификатору.
     *
     * @param id уникальный идентификатор комментария для удаления
     */
    void deleteById(Long id);
}