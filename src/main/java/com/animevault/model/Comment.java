package com.animevault.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", nullable = false)
    private Video video;

    // null = anonymous
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotBlank
    @Size(min = 1, max = 2000)
    private String content;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    // Displayed username (anonymous if user is null)
    public String getDisplayName() {
        return (user != null) ? user.getUsername() : "Anonymous";
    }

    @PrePersist
    public void prePersist() {
        this.timestamp = LocalDateTime.now();
    }
}
