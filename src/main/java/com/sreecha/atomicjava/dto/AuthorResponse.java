package com.sreecha.atomicjava.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response DTO for Author details")
public class AuthorResponse {
    @Schema(description = "Unique identifier of the author", example = "1")
    private Long id;

    @Schema(description = "Name of the author", example = "Douglas Adams")
    private String name;

    @Schema(description = "Biography of the author", example = "English author, humorist, and dramatist.")
    private String biography;

    @Schema(description = "Date of birth of the author (YYYY-MM-DD)", example = "1952-03-11")
    private LocalDate dob;
}