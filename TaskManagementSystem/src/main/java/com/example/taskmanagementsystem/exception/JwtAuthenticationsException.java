package com.example.taskmanagementsystem.exception;

/**
 * Исключение, связанное с аутентификацией по JWT.
 */
public class JwtAuthenticationsException extends RuntimeException {
    public JwtAuthenticationsException(String msg) {
        super(msg);
    }
}