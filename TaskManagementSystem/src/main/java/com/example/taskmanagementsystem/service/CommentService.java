package com.example.taskmanagementsystem.service;

import com.example.taskmanagementsystem.dto.comment.CommentDto;
import com.example.taskmanagementsystem.dto.comment.CommentFilterDto;
import com.example.taskmanagementsystem.dto.comment.CreateCommentDto;
import org.springframework.lang.Nullable;

import java.util.List;


public interface CommentService {

    CommentDto save(CreateCommentDto commentDto);


    List<CommentDto> findAll(@Nullable CommentFilterDto commentFilterDto);

    default List<CommentDto> findAll() {
        return findAll(null);
    }

    CommentDto findById(Long id);


    void deleteById(Long id);
}