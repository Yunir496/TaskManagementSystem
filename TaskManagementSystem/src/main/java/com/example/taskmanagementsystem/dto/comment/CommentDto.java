package com.example.taskmanagementsystem.dto.comment;

import com.example.taskmanagementsystem.entity.Comment;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;

import java.util.Date;


@Data
public class CommentDto {
    private String text;
    private String authorName;
    private Date date;


    public static CommentDto fromComment(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setText(comment.getText());
        commentDto.setDate(comment.getDate());
        if (comment.getAuthor() != null
                && Strings.isNotEmpty(comment.getAuthor().getFirstName()) && Strings.isNotEmpty(comment.getAuthor().getLastName())) {
            commentDto.setAuthorName(comment.getAuthor().getFirstName() + " " + comment.getAuthor().getLastName());
        }
        return commentDto;
    }


    public Comment toComment(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setDate(commentDto.getDate());
        return comment;
    }
}