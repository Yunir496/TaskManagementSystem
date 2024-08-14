package com.example.taskmanagementsystem.exception;

/**
 * Исключение, связанное с недостаточными правами у пользователя.
 */
public class InsufficientPermissionsException extends RuntimeException {
    public InsufficientPermissionsException(String message) {
        super(message);
    }
}