package com.animevault.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "videos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    @NotBlank
    @Size(max = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private VideoType type;  // SERIES or MOVIE

    @Column(length = 500)
    private String thumbnailUrl;

    // Only used for MOVIE type
    @Column(length = 1000)
    private String videoUrl;

    @DecimalMin("0.0")
    @DecimalMax("10.0")
    private Double rating;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Genres stored as comma-separated string for simplicity
    @Column(length = 500)
    private String genres;

    // For series: release year
    private Integer releaseYear;

    // Studio / author
    @Column(length = 100)
    private String studio;

    // Status: ONGOING, COMPLETED, UPCOMING
    @Column(length = 20)
    private String status;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("seasonNumber ASC")
    @Builder.Default
    private List<Season> seasons = new ArrayList<>();

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public String[] getGenreArray() {
        if (genres == null || genres.isBlank()) return new String[0];
        return genres.split(",");
    }

    public enum VideoType {
        SERIES, MOVIE
    }
}
