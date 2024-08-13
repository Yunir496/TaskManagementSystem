package com.example.taskmanagementsystem.dto.user;

import com.example.taskmanagementsystem.entity.User;
import lombok.Data;


@Data
public class CreateUserDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean isUser;

    public User toUser(){
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }
}
