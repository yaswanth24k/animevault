package com.animevault.dto;

import com.animevault.model.Video.VideoType;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoForm {

    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must be under 200 characters")
    private String title;

    @Size(max = 5000, message = "Description too long")
    private String description;

    @NotNull(message = "Type is required")
    private VideoType type;

    @Size(max = 500, message = "Thumbnail URL too long")
    private String thumbnailUrl;

    // Only for MOVIE
    @Size(max = 1000, message = "Video URL too long")
    private String videoUrl;

    @DecimalMin(value = "0.0", message = "Rating must be 0–10")
    @DecimalMax(value = "10.0", message = "Rating must be 0–10")
    private Double rating;

    private String genres; // comma-separated

    private Integer releaseYear;

    @Size(max = 100, message = "Studio name too long")
    private String studio;

    private String status;
}
