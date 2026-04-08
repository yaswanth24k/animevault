package com.animevault.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EpisodeForm {

    private Long id;

    @NotNull(message = "Episode number is required")
    @Min(value = 1, message = "Episode number must be at least 1")
    private Integer episodeNumber;

    @NotBlank(message = "Episode title is required")
    @Size(max = 200, message = "Title must be under 200 characters")
    private String title;

    @Size(max = 5000, message = "Description too long")
    private String description;

    @NotBlank(message = "Video URL is required")
    @Size(max = 1000, message = "Video URL too long")
    private String videoUrl;

    @Size(max = 500, message = "Thumbnail URL too long")
    private String thumbnailUrl;

    @DecimalMin(value = "0.0", message = "Rating must be between 0 and 10")
    @DecimalMax(value = "10.0", message = "Rating must be between 0 and 10")
    private Double rating;

    @Min(value = 1, message = "Duration must be positive")
    private Integer durationMinutes;
}
