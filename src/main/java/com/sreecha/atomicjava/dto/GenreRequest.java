package com.sreecha.atomicjava.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenreRequest {
    @NotBlank(message = "Genre name cannot be empty")
    @Size(max = 100, message = "Genre name cannot exceed 100 characters")
    private String name;

    private String description;
}