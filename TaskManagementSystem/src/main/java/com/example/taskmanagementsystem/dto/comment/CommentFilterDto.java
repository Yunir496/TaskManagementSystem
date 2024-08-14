package com.example.taskmanagementsystem.dto.comment;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * DTO для фильтрации комментариев
 */
@Data
@Builder
public class CommentFilterDto {
    private String text;
    private String authorName;
    private Date date;
    private Long taskId;
    private Integer page;
    private Integer size;
}