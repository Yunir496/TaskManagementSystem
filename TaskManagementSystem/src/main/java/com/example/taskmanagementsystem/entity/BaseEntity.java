package com.example.taskmanagementsystem.entity;

import com.example.taskmanagementsystem.entity.enums.Status;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

/**
 * Базовый класс для всех сущностей, содержащий общие поля.
 */
@Data
@MappedSuperclass
public class BaseEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @CreatedDate
    private Date created;
    @LastModifiedDate
    private Date updated;
    @Enumerated(EnumType.STRING)
    private Status status;
}