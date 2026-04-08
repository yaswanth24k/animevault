package com.animevault.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "episodes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Episode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;

    @Column(nullable = false)
    @Min(1)
    private Integer episodeNumber;

    @Column(nullable = false, length = 200)
    @NotBlank
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 1000)
    @NotBlank
    private String videoUrl;

    @Column(length = 500)
    private String thumbnailUrl;

    @DecimalMin("0.0")
    @DecimalMax("10.0")
    private Double rating;

    // Duration in minutes
    private Integer durationMinutes;
}
