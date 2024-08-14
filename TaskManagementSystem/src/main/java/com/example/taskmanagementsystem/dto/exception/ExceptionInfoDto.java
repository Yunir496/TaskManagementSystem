package com.example.taskmanagementsystem.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO для информации об исключении.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionInfoDto {
    private Integer statusCode;
    private String message;
    private LocalDateTime date;
}