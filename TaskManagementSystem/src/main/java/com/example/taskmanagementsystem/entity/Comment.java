package com.example.taskmanagementsystem.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;
    @Column(name = "created_date")
    private Date date;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id + ", text='" + text + '\'' +
                (author != null ? ", author=" + author.getId() : "") + ", date='" + date + '\'' +
                (task != null ? ", task=" + task.getId() : "") + '}';
    }
}