package com.sreecha.atomicjava.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;


import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request DTO for Author creation and update")
public class AuthorRequest {
    @NotBlank(message = "Author name cannot be empty")
    @Size(max = 255, message = "Author name cannot exceed 255 characters")
    @Schema(description = "Name of the author", example = "Douglas Adams")
    private String name;

    @Schema(description = "Biography of the author", example = "English author, humorist, and dramatist.")
    private String biography;

    @PastOrPresent(message = "Birth date cannot be in the future")
    @Schema(description = "Date of birth of the author (YYYY-MM-DD)", example = "1952-03-11")
    private LocalDate dob;
}