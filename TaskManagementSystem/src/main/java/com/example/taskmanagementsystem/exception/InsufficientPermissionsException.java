package com.example.taskmanagementsystem.exception;

public class InsufficientPermissionsException extends RuntimeException {

    public InsufficientPermissionsException(String message) {
        super(message);
    }
}