package com.example.taskmanagementsystem.service.impl;

import com.example.taskmanagementsystem.dao.RoleRepository;
import com.example.taskmanagementsystem.dao.UserRepository;
import com.example.taskmanagementsystem.dto.user.UserDto;
import com.example.taskmanagementsystem.entity.Role;
import com.example.taskmanagementsystem.entity.enums.Status;
import com.example.taskmanagementsystem.entity.User;
import com.example.taskmanagementsystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    @Transactional
    public UserDto register(User user, boolean isUser) {
        Role defaultRole;
        if (isUser) {
            defaultRole = roleRepository.findByName("ROLE_USER");
        } else {
            defaultRole = roleRepository.findByName("ROLE_EXECUTOR");
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singletonList(defaultRole));
        user.setStatus(Status.ACTIVE);
        user.setCreated(new Date());
        user.setUpdated(new Date());
        User registeredUser = userRepository.save(user);
        log.info("In register user {} successfully registered with role {}", registeredUser, defaultRole);
        return UserDto.fromUser(registeredUser);
    }

    @Override
    @Transactional
    public List<UserDto> getAll() {
        List<User> result = userRepository.findAll();
        log.info("In getAll {} users found", result.size());
        return result.stream().map(UserDto::fromUser).toList();
    }

    @Override
    @Transactional
    public User findByEmail(String email) {
        User result = userRepository.findByEmail(email);
        log.info("In findByEmail user {} found by email {}", result, email);
        return result;
    }

    @Override
    @Transactional
    public User findById(Long id) {
        Optional<User> result = userRepository.findById(id);
        if (result.isEmpty()) {
            log.warn("In findById no user found by id {}", id);
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
        User user = result.get();
        log.info("In findById user {} found by id {}", user, id);
        return user;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            log.warn("In delete user with id {} not found", id);
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
        userRepository.delete(optionalUser.get());
        log.info("In delete user with id {} found and successfully deleted", id);
    }
}