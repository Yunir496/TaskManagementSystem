package com.example.taskmanagementsystem.service;


import com.example.taskmanagementsystem.entity.User;

import java.util.List;

public interface UserService {
    User register(User user,boolean isUser);
    List<User> getAll();
    User findByEmail(String email);
    User findById(Long id);
    void delete(Long id);
}
