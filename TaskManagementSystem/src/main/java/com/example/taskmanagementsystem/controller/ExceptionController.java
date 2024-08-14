package com.example.taskmanagementsystem.controller;

import com.example.taskmanagementsystem.dto.exception.ExceptionInfoDto;
import com.example.taskmanagementsystem.exception.InsufficientPermissionsException;
import com.example.taskmanagementsystem.exception.JwtAuthenticationsException;
import com.example.taskmanagementsystem.exception.UserNotRegisteredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler
    public ResponseEntity<ExceptionInfoDto> handleException(Exception e) {
        ExceptionInfoDto exceptionInfoDto = new ExceptionInfoDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(exceptionInfoDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionInfoDto> handleBindException(BindException e) {
        ExceptionInfoDto exceptionInfoDto = new ExceptionInfoDto(HttpStatus.BAD_REQUEST.value(), e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(exceptionInfoDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionInfoDto> handleUserNotRegisteredException(UserNotRegisteredException e) {
        ExceptionInfoDto exceptionInfoDto = new ExceptionInfoDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(exceptionInfoDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionInfoDto> handleEntityNotFoundException(EntityNotFoundException e) {
        ExceptionInfoDto exceptionInfoDto = new ExceptionInfoDto(HttpStatus.NOT_FOUND.value(), e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(exceptionInfoDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionInfoDto> handleUsernameNotFoundException(UsernameNotFoundException e) {
        ExceptionInfoDto exceptionInfoDto = new ExceptionInfoDto(HttpStatus.FORBIDDEN.value(), e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(exceptionInfoDto, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionInfoDto> handleBadCredentialsException(BadCredentialsException e) {
        ExceptionInfoDto exceptionInfoDto = new ExceptionInfoDto(HttpStatus.BAD_REQUEST.value(), e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(exceptionInfoDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionInfoDto> handleInsufficientPermissionsException(InsufficientPermissionsException e) {
        ExceptionInfoDto exceptionInfoDto = new ExceptionInfoDto(HttpStatus.FORBIDDEN.value(), e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(exceptionInfoDto, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionInfoDto> handleJwtAuthenticationsException(JwtAuthenticationsException e) {
        ExceptionInfoDto exceptionInfoDto = new ExceptionInfoDto(HttpStatus.BAD_REQUEST.value(), e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(exceptionInfoDto, HttpStatus.BAD_REQUEST);
    }
}