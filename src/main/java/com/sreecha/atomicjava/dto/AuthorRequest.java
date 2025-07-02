package com.sreecha.atomicjava.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorRequest {
    @NotBlank(message = "Author name cannot be empty")
    @Size(max = 255, message = "Author name cannot exceed 255 characters")
    private String name;

    private String biography;

    @PastOrPresent(message = "Birth date cannot be in the future")
    private LocalDate dob;
}