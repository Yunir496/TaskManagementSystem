package com.example.taskmanagementsystem.service;

import com.example.taskmanagementsystem.dto.auth.AuthenticationRequestDto;
import com.example.taskmanagementsystem.dto.user.CreateUserDto;
import com.example.taskmanagementsystem.dto.user.UserDto;

import java.util.Map;

public interface AuthenticationService {

    Map<Object, Object> authenticateUser(AuthenticationRequestDto requestDto);


    UserDto createUser(CreateUserDto createUserDto);
}