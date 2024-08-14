package com.example.taskmanagementsystem.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO для запроса аутентификации.
 */
@Data
@AllArgsConstructor
public class AuthenticationRequestDto {
    private String email;
    private String password;
}