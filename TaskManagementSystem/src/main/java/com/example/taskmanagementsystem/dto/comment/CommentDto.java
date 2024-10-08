package com.example.taskmanagementsystem.dto.comment;

import com.example.taskmanagementsystem.entity.Comment;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;

import java.util.Date;

/**
 * DTO для комментариев.
 */
@Data
public class CommentDto {
    private String text;
    private String authorName;
    private Date date;

    /**
     * Преобразует объект Comment в CommentDto.
     *
     * @param comment объект комментария
     * @return CommentDto
     */
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

    /**
     * Преобразует CommentDto в объект Comment.
     *
     * @param commentDto CommentDto для преобразования
     * @return Comment объект комментария
     */
    public Comment toComment(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setDate(commentDto.getDate());
        return comment;
    }
}