package com.animevault.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeasonForm {

    private Long id;

    @NotNull(message = "Season number is required")
    @Min(value = 1, message = "Season number must be at least 1")
    private Integer seasonNumber;

    @Size(max = 200)
    private String title;

    @Size(max = 5000)
    private String description;

    @Size(max = 500)
    private String thumbnailUrl;
}
