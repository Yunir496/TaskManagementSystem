package com.example.taskmanagementsystem.dao;

import com.example.taskmanagementsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository <User,Long>{
    User findByEmail(String email);

}
