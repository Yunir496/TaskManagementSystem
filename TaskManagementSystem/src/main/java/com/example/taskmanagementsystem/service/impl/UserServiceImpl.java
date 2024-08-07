package com.example.taskmanagementsystem.service.impl;

import com.example.taskmanagementsystem.dao.RoleRepository;
import com.example.taskmanagementsystem.dao.UserRepository;
import com.example.taskmanagementsystem.entity.Role;
import com.example.taskmanagementsystem.entity.Status;
import com.example.taskmanagementsystem.entity.User;

import com.example.taskmanagementsystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
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
    public User register(User user) {
        Role defaultRole = roleRepository.findByName("ROLE_USER");
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singletonList(defaultRole));
        user.setStatus(Status.ACTIVE);
        User registeredUser = userRepository.save(user);
        log.info("In register user {} successfully registered", registeredUser);
        return registeredUser;
    }
    @Override
    public List<User> getAll() {
        List<User> result = userRepository.findAll();
        log.info("In getAll {} users found", result.size());
        return result;
    }
    @Override
    public User findByEmail(String email) {
        User result = userRepository.findByEmail(email);
        log.info("In findByEmail user {} found by email {}", result, email);
        return result;
    }
    @Override
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
    public void delete(Long id) {
        userRepository.deleteById(id);
        log.info("In delete user with id {} successfully deleted", id);
    }
}
