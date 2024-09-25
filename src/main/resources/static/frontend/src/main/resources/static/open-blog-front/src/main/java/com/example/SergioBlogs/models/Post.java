package com.example.SergioBlogs.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @NotBlank
    @Column(name = "title")
    private String title;

    @NotBlank
    @Column(name = "body", columnDefinition = "text")
    private String body;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false)
    private Account account;

    @Override
    public String toString(){
        return "Account{" +
                "id=" + id +
                ", title='" + title + "'" +
                ", body='" + body + "'" +
                ", createdAt='" + createdAt + "'" +
                ", modifiedAt='" + modifiedAt +
                "}";
    }
}
