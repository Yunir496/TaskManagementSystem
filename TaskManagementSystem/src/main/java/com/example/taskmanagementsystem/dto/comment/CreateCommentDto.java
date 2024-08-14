package com.example.taskmanagementsystem.dto.comment;

import com.example.taskmanagementsystem.entity.Comment;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * DTO для создания комментариев.
 */
@Data
@Builder
public class CreateCommentDto {
    private Long id;
    @NotBlank
    private String text;
    @NotNull
    private Long authorId;
    @NotNull
    private Date date;
    @NotNull
    private Long taskId;

    /**
     * Преобразует объект CreateCommentDto в объект Comment.
     *
     * @return Comment объект комментария
     */
    public Comment toComment() {
        Comment comment = new Comment();
        comment.setId(id != null ? id : null);
        comment.setText(text);
        comment.setDate(date);
        return comment;
    }
}