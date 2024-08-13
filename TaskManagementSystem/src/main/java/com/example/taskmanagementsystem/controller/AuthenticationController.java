package com.example.taskmanagementsystem.controller;
import com.example.taskmanagementsystem.dto.auth.AuthenticationRequestDto;
import com.example.taskmanagementsystem.dto.user.CreateUserDto;
import com.example.taskmanagementsystem.dto.user.UserDto;
import com.example.taskmanagementsystem.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/api/v1/auth/")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("login")
    public ResponseEntity<Map<Object, Object>> login(@RequestBody AuthenticationRequestDto requestDto) {
        Map<Object, Object> response = authenticationService.authenticateUser(requestDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("create")
    public ResponseEntity<UserDto> createUser(@RequestBody CreateUserDto createUserDto) {
        return ResponseEntity.ok(authenticationService.createUser(createUserDto));
    }
}
