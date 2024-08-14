package com.example.taskmanagementsystem.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * Класс для сущности ролей пользователей.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
@Entity
public class Role extends BaseEntity {
    public Role(String name) {
        this.name = name;
    }

    private String name;
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<User> users;

    @Override
    public String toString() {
        return "Role{" + "name='" + name + '\'' +
                (users != null ? ", users=" + users.stream().map(BaseEntity::getId).toList() : "") + '}';
    }
}