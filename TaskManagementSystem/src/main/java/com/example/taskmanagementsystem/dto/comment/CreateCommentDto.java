package com.example.taskmanagementsystem.dto.comment;

import com.example.taskmanagementsystem.entity.Comment;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CreateCommentDto {
    private String text;
    private Long authorId;
    private Date date;
    private Long taskId;


    public Comment toComment() {
        Comment comment = new Comment();
        comment.setText(text);
        comment.setDate(date);
        return comment;
    }
}