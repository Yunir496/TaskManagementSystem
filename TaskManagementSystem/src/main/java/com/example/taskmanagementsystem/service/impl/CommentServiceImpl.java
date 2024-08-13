package com.example.taskmanagementsystem.service.impl;

import com.example.taskmanagementsystem.dao.CommentRepository;
import com.example.taskmanagementsystem.dao.TaskRepository;
import com.example.taskmanagementsystem.dto.comment.CommentDto;
import com.example.taskmanagementsystem.dto.comment.CommentFilterDto;
import com.example.taskmanagementsystem.dto.comment.CreateCommentDto;
import com.example.taskmanagementsystem.entity.Comment;
import com.example.taskmanagementsystem.entity.Task;
import com.example.taskmanagementsystem.entity.User;
import com.example.taskmanagementsystem.entity.utils.CommentSpecification;
import com.example.taskmanagementsystem.service.CommentService;
import com.example.taskmanagementsystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final TaskRepository taskRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, UserService userService, TaskRepository taskRepository) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.taskRepository = taskRepository;
    }

    @Override
    @Transactional
    public CommentDto save(CreateCommentDto commentDto) {
        User author = userService.findById(commentDto.getAuthorId());
        Optional<Task> optionalTask = taskRepository.findById(commentDto.getTaskId());
        if (optionalTask.isEmpty()) {
            log.warn("In save task with id {} not found", commentDto.getTaskId());
            throw new EntityNotFoundException("Task with id " + commentDto.getTaskId() + " not found");
        }
        Comment comment = commentDto.toComment();
        comment.setAuthor(author);
        comment.setTask(optionalTask.get());
        log.info("In save comment {} successfully saved", comment);
        return CommentDto.fromComment(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public List<CommentDto> findAll(@Nullable CommentFilterDto commentFilterDto) {
        if (commentFilterDto == null) {
            List<Comment> comments = commentRepository.findAll();
            log.info("In findAll found {} comment(s) without filtration and pagination", comments.size());
            return comments.stream().map(CommentDto::fromComment).toList();
        }
        Pageable pageable = createPageable(commentFilterDto);
        Specification<Comment> specification = CommentSpecification.commentFilter(commentFilterDto);
        if (pageable == null) {
            List<Comment> comments = commentRepository.findAll(specification);
            log.info("In findAll found {} comment(s) without pagination", comments.size());
            return comments.stream().map(CommentDto::fromComment).toList();
        }
        Page<Comment> comments = commentRepository.findAll(specification, pageable);
        log.info("In findAll found {} comment(s) with filtration and pagination", comments.getSize());
        return comments.stream().map(CommentDto::fromComment).toList();
    }

    @Override
    @Transactional
    public CommentDto findById(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isEmpty()) {
            log.warn("In findById comment with id {} not found", id);
            throw new EntityNotFoundException("Comment with id " + id + " not found");
        }
        log.info("In findById comment {} successfully found", comment);
        return CommentDto.fromComment(comment.get());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (optionalComment.isEmpty()) {
            log.warn("In deleteById comment with id {} not found", id);
            throw new EntityNotFoundException("Comment with id " + id + " not found");
        }
        commentRepository.delete(optionalComment.get());
        log.info("In deleteById comment with id {} found and successfully deleted", id);
    }

    private Pageable createPageable(CommentFilterDto commentFilterDto) {
        Pageable pageable = null;
        if (commentFilterDto.getPage() != null && commentFilterDto.getSize() != null) {
            log.info("In createPageable found limit {} for page {}", commentFilterDto.getSize(), commentFilterDto.getPage());
            pageable = PageRequest.of(commentFilterDto.getPage(), commentFilterDto.getSize());
        }
        return pageable;
    }
}