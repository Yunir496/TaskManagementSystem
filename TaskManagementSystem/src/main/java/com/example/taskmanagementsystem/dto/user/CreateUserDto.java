package com.example.taskmanagementsystem.dto.user;

import com.example.taskmanagementsystem.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * DTO для создания пользователей.
 */
@Data
@Builder
public class CreateUserDto {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private boolean isUser;

    /**
     * Преобразует объект CreateUserDto в объект User.
     *
     * @return User объект пользователя
     */
    public User toUser() {
        return User.builder()
                .firstName(firstName).lastName(lastName)
                .email(email).password(password)
                .build();
    }
}