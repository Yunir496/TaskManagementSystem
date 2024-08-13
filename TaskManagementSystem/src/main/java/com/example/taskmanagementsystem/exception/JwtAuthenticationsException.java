package com.example.taskmanagementsystem.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationsException extends AuthenticationException {
    public JwtAuthenticationsException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public JwtAuthenticationsException(String msg) {
        super(msg);
    }
}
