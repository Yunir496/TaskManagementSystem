package com.example.taskmanagementsystem.service;

import com.example.taskmanagementsystem.dto.user.UserDto;
import com.example.taskmanagementsystem.entity.User;

import java.util.List;

/**
 * Сервисный слой для управления пользователями.
 */
public interface UserService {
    /**
     * Регистрация нового пользователя.
     *
     * @param user данные нового пользователя
     * @param isUser флаг, указывающий является ли пользователь обычным пользователем или исполнителем
     * @return зарегистрированный пользователь
     */
    UserDto register(User user, boolean isUser);

    /**
     * Получить список всех пользователей.
     * @return список всех пользователей
     */
    List<UserDto> getAll();

    /**
     * Найти пользователя по адресу электронной почты.
     *
     * @param email адрес электронной почты пользователя
     * @return найденный пользователь или null, если не найден
     */
    User findByEmail(String email);

    /**
     * Найти пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     * @return найденный пользователь или null, если не найден
     */
    User findById(Long id);

    /**
     * Удалить пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     */
    void delete(Long id);
}