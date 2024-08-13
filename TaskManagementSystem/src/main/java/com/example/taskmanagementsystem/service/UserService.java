package com.example.taskmanagementsystem.service;


import com.example.taskmanagementsystem.dto.user.UserDto;
import com.example.taskmanagementsystem.entity.User;

import java.util.List;

public interface UserService {

    UserDto register(User user, boolean isUser);

    List<UserDto> getAll();

    User findByEmail(String email);

    User findById(Long id);

    void delete(Long id);
}