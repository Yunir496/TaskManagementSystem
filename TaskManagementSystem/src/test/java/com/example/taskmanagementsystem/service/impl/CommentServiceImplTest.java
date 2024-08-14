package com.example.taskmanagementsystem.service.impl;

import com.example.taskmanagementsystem.dao.CommentRepository;
import com.example.taskmanagementsystem.dao.TaskRepository;
import com.example.taskmanagementsystem.dto.comment.CommentDto;
import com.example.taskmanagementsystem.dto.comment.CommentFilterDto;
import com.example.taskmanagementsystem.dto.comment.CreateCommentDto;
import com.example.taskmanagementsystem.entity.Comment;
import com.example.taskmanagementsystem.entity.Task;
import com.example.taskmanagementsystem.entity.User;
import com.example.taskmanagementsystem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserService userService;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    private CreateCommentDto commentDto;
    private List<Comment> comments;

    @BeforeEach
    void setUp() {
        commentDto = CreateCommentDto.builder()
                .authorId(1L)
                .date(new Date())
                .text("test")
                .taskId(1L)
                .build();

        Comment comment = Comment.builder()
                .text("comment")
                .date(new Date())
                .build();
        Comment comment1 = Comment.builder()
                .text("comment1")
                .date(new Date())
                .build();
        Comment comment2 = Comment.builder()
                .text("comment1")
                .date(new Date())
                .build();
        comments = List.of(comment, comment1, comment2);
    }

    @Test
    void testSaveWithNonExistingAuthor() {
        // Тест сценария, когда автор не найден
        when(userService.findById(anyLong())).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> commentService.save(commentDto));
    }

    @Test
    void testSaveWithNonExistingTask() {
        // Тест сценария, когда задача не найдена
        when(userService.findById(anyLong())).thenReturn(User.builder().build());
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        Throwable throwable = assertThrows(EntityNotFoundException.class, () -> commentService.save(commentDto));
        assertNotNull(throwable);
        assertEquals("Task with id " + commentDto.getTaskId() + " not found", throwable.getMessage());
    }

    @Test
    void testSaveWithAllDataFound() {
        // Тест сценария, когда все данные найдены
        when(userService.findById(anyLong())).thenReturn(User.builder().build());
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(new Task()));
        when(commentRepository.save(any(Comment.class))).thenReturn(commentDto.toComment());

        CommentDto result = commentService.save(commentDto);
        assertNotNull(result);
        assertEquals(result.getDate(), commentDto.getDate());
        assertEquals(result.getText(), commentDto.getText());
    }

    @Test
    void testFindAllWithNullCommentFilterDto() {
        // Тест сценария, когда commentFilterDto == null
        when(commentRepository.findAll()).thenReturn(comments);

        List<CommentDto> result = commentService.findAll(null);
        assertEquals(comments.size(), result.size());
    }

    @Test
    void testFindAllWithNullPageable() {
        // Тест сценария, когда pageable == null
        List<Comment> expectedComments = comments.subList(0, 2);
        when(commentRepository.findAll(any(Specification.class))).thenReturn(expectedComments);

        CommentFilterDto commentFilterDto = CommentFilterDto.builder()
                .build();
        List<CommentDto> result = commentService.findAll(commentFilterDto);
        assertEquals(expectedComments.size(), result.size());
    }

    @Test
    void testFindAllWithAllData() {
        // Тест сценария, когда все данные есть
        Page<Comment> specComments = new PageImpl<>(comments.subList(0, 2), PageRequest.of(0, 2), comments.size());
        when(commentRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(specComments);

        CommentFilterDto commentFilterDto = CommentFilterDto.builder()
                .page(0)
                .size(2)
                .build();
        List<CommentDto> result = commentService.findAll(commentFilterDto);
        assertEquals(result.size(), specComments.getSize());
    }

    @Test
    void testFindByIdWithNonExistingComment() {
        // Тест сценария, когда комментарий не найден
        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());

        Throwable throwable = assertThrows(EntityNotFoundException.class, () -> commentService.findById(1L));
        assertNotNull(throwable);
        assertEquals("Comment with id 1 not found", throwable.getMessage());
    }

    @Test
    void testFindByIdWithExistingComment() {
        // Тест сценария, когда все данные найдены
        Comment comment = Comment.builder()
                .text("comment")
                .date(new Date())
                .build();
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));

        CommentDto result = commentService.findById(1L);
        assertNotNull(result);
        assertEquals(result.getDate(), comment.getDate());
        assertEquals(result.getText(), comment.getText());
    }

    @Test
    void testDeleteByIdWithExistingComment() {
        // Тест сценария, когда комментарий найден
        Comment expectedComment = Comment.builder().text("text").build();
        Optional<Comment> optionalComment = Optional.of(expectedComment);
        when(commentRepository.findById(anyLong())).thenReturn(optionalComment);

        commentService.deleteById(1L);
        verify(commentRepository).delete(expectedComment);
    }

    @Test
    void testDeleteByIdWithNonExistingComment() {
        // Тест сценария, когда комментарий не найден
        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());

        Throwable throwable = assertThrows(EntityNotFoundException.class, () -> commentService.deleteById(1L));
        assertNotNull(throwable);
        assertEquals("Comment with id 1 not found", throwable.getMessage());
    }
}