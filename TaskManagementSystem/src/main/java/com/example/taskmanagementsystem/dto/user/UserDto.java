package com.example.taskmanagementsystem.dto.user;

import com.example.taskmanagementsystem.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * DTO для пользователей.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private String firstName;
    private String lastName;
    private String email;

    /**
     * Преобразует объект UserDto в объект User.
     *
     * @return User объект пользователя
     */
    public User toUser() {
        return User.builder()
                .firstName(firstName).lastName(lastName)
                .email(email).build();
    }

    /**
     * Преобразует объект User в объект UserDto.
     * @param user объект пользователя
     *
     * @return UserDto объект пользователя
     */
    public static UserDto fromUser(User user) {
        UserDto userDto = new UserDto();
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }
}