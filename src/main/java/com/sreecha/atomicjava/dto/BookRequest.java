package com.sreecha.atomicjava.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {
    @NotBlank(message = "Title cannot be empty")
    @Size(max = 500, message = "Title cannot exceed 500 characters")
    private String title;

    @NotBlank(message = "ISBN cannot be empty")
    @Size(min = 10, max = 13, message = "ISBN must be between 10 and 13 characters")
    private String isbn;

    @NotNull(message = "Publication year cannot be empty")
    @Min(value = 1000, message = "Publication year must be a valid year")
    private Integer publicationYear;

    private String description;
    private String coverImageUrl;

    @NotNull(message = "Author ID cannot be empty")
    private Long authorId;

    private Set<Long> genreIds;
}