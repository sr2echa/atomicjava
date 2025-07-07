package com.sreecha.atomicjava.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Schema(description = "Request DTO for Genre creation and update")
public class GenreRequest {
    @NotBlank(message = "Genre name cannot be empty")
    @Size(max = 100, message = "Genre name cannot exceed 100 characters")
    @Schema(description = "Name of the genre", example = "Science Fiction")
    private String name;

    @Schema(description = "Description of the genre", example = "Books that explore imaginative and futuristic concepts.")
    private String description;
}