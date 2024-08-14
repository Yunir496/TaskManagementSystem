package com.example.taskmanagementsystem.service;

import com.example.taskmanagementsystem.dto.auth.AuthenticationRequestDto;
import com.example.taskmanagementsystem.dto.user.CreateUserDto;
import com.example.taskmanagementsystem.dto.user.UserDto;

import java.util.Map;

/**
 * Сервис для аутентификации и создания пользователей.
 */
public interface AuthenticationService {
    /**
     * Выполняет аутентификацию пользователя на основе переданного запроса.
     * @param requestDto объект с данными запроса аутентификации
     *
     * @return Map с данными пользователя и токеном аутентификации
     */
    Map<Object, Object> authenticateUser(AuthenticationRequestDto requestDto);

    /**
     * Создает нового пользователя на основе переданных данных.
     * @param createUserDto объект с данными для создания нового пользователя
     *
     * @return объект UserDto, представляющий созданного пользователя
     */
    UserDto createUser(CreateUserDto createUserDto);
}