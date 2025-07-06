package com.sreecha.atomicjava.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response DTO for Genre details")
public class GenreResponse {
    @Schema(description = "Unique identifier of the genre", example = "1")
    private Long id;

    @Schema(description = "Name of the genre", example = "Science Fiction")
    private String name;

    @Schema(description = "Description of the genre", example = "Books that explore imaginative and futuristic concepts.")
    private String description;
}