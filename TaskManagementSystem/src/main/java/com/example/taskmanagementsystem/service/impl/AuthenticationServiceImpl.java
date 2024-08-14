package com.example.taskmanagementsystem.service.impl;

import com.example.taskmanagementsystem.dto.auth.AuthenticationRequestDto;
import com.example.taskmanagementsystem.dto.user.CreateUserDto;
import com.example.taskmanagementsystem.dto.user.UserDto;
import com.example.taskmanagementsystem.entity.User;
import com.example.taskmanagementsystem.exception.UserNotRegisteredException;
import com.example.taskmanagementsystem.security.jwt.JwtTokenProvider;
import com.example.taskmanagementsystem.service.AuthenticationService;
import com.example.taskmanagementsystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Autowired
    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @Override
    public Map<Object, Object> authenticateUser(AuthenticationRequestDto requestDto) {
        try {
            String email = requestDto.getEmail();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, requestDto.getPassword()));
            User user = userService.findByEmail(email);
            if (user == null) {
                log.warn("In authenticateUser user with email: {} not found", requestDto.getEmail());
                throw new UsernameNotFoundException("User with email " + email + " not found");
            }
            String token = jwtTokenProvider.createToken(email, user.getRoles());
            Map<Object, Object> response = new HashMap<>();
            response.put("email", email);
            response.put("token", token);
            log.info("In authenticateUser user with email: {} successfully authenticate", email);
            return response;
        } catch (AuthenticationException e) {
            log.warn("In authenticateUser insert invalid email or password");
            throw new BadCredentialsException("Invalid email or password");
        }
    }

    @Override
    public UserDto createUser(CreateUserDto createUserDto) {
        User user = createUserDto.toUser();
        UserDto register = userService.register(user, createUserDto.isUser());
        if (register == null) {
            log.warn("In registerUser user with email: {} not registered", createUserDto.getEmail());
            throw new UserNotRegisteredException("User " + createUserDto + " not registered");
        }
        log.info("In registerUser user: {} registered successfully", register);
        return register;
    }
}