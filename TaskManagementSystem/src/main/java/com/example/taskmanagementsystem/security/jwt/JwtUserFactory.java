package com.example.taskmanagementsystem.security.jwt;

import com.example.taskmanagementsystem.entity.Role;
import com.example.taskmanagementsystem.entity.enums.Status;
import com.example.taskmanagementsystem.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Фабрика для создания объектов JwtUser на основе объектов User.
 */
public final class JwtUserFactory {
    public JwtUserFactory() {
    }

    /**
     * Создает объект JwtUser на основе объекта User.
     *
     * @param user объект пользователя
     * @return объект JwtUser
     */
    public static JwtUser create(User user) {
        return new JwtUser(user.getId(), user.getFirstName(),
                user.getLastName(), user.getEmail(),
                user.getPassword(), user.getStatus().equals(Status.ACTIVE),
                user.getUpdated(), mapToGrantedAuthorities(new ArrayList<>(user.getRoles())));
    }

    /**
     * Преобразует список ролей пользователя в список объектов GrantedAuthority.
     * @param userRoles список ролей пользователя
     *
     * @return список объектов GrantedAuthority
     */
    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Role> userRoles) {
        return userRoles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}