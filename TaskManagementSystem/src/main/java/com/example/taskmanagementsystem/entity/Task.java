package com.example.taskmanagementsystem.entity;

import com.example.taskmanagementsystem.entity.enums.TaskPriority;
import com.example.taskmanagementsystem.entity.enums.TaskStatus;
import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * Класс для сущности задач.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    @Enumerated(EnumType.STRING)
    private TaskPriority priority;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "executor_id")
    private User executor;
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;

    @Override
    public String toString() {
        return "Task{" + "id=" + id +
                ", title='" + title + '\'' + ", description='" + description + '\'' +
                ", status=" + status + ", priority=" + priority +
                (creator != null ? ", creator=" + creator.getId() : "") + ", comments=" + comments +
                (executor != null ? ", executor=" + executor.getId() : "") + '}';
    }
}