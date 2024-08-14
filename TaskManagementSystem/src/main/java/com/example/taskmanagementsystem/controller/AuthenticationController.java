package com.example.taskmanagementsystem.controller;

import com.example.taskmanagementsystem.dto.auth.AuthenticationRequestDto;
import com.example.taskmanagementsystem.dto.user.CreateUserDto;
import com.example.taskmanagementsystem.dto.user.UserDto;
import com.example.taskmanagementsystem.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Контроллер аутентификации и создания пользователей.
 */
@RestController
@RequestMapping("/api/v1/auth/")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Обработка запроса на авторизацию.
     * @param requestDto объект с запросом аутентификации
     *
     * @return ответ с токеном авторизации и информацией о пользователе
     */
    @PostMapping("login")
    public ResponseEntity<Map<Object, Object>> login(@RequestBody AuthenticationRequestDto requestDto) {
        Map<Object, Object> response = authenticationService.authenticateUser(requestDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Обработка запроса на создание нового пользователя.
     *
     * @param createUserDto объект для создания нового пользователя
     * @return ответ с информацией о созданном пользователе
     */
    @PostMapping("create")
    public ResponseEntity<UserDto> createUser(@RequestBody @Validated CreateUserDto createUserDto) {
        return ResponseEntity.ok(authenticationService.createUser(createUserDto));
    }
}