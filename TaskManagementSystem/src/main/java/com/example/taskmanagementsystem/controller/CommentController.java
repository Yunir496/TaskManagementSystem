package com.example.taskmanagementsystem.controller;

import com.example.taskmanagementsystem.dto.comment.CommentDto;
import com.example.taskmanagementsystem.dto.comment.CommentFilterDto;
import com.example.taskmanagementsystem.dto.comment.CreateCommentDto;
import com.example.taskmanagementsystem.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/v1/comments/")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("all")
    public ResponseEntity<List<CommentDto>> getAllComments(@RequestBody(required = false) CommentFilterDto commentFilterDto) {
        return ResponseEntity.ok(commentService.findAll(commentFilterDto));
    }

    @GetMapping("{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable("id") @Validated @NotNull Long id) {
        return ResponseEntity.ok(commentService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CommentDto> createComment(@RequestBody @Validated CreateCommentDto commentDto) {
        return ResponseEntity.ok(commentService.save(commentDto));
    }

    @PutMapping
    public ResponseEntity<CommentDto> updateComment(@RequestBody @Validated CreateCommentDto commentDto) {
        return ResponseEntity.ok(commentService.save(commentDto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<CommentDto> deleteComment(@PathVariable("id") @Validated @NotNull Long id) {
        commentService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}