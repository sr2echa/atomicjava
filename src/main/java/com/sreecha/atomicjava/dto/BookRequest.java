package com.sreecha.atomicjava.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request DTO for Book creation and update")
public class BookRequest {
    @NotBlank(message = "Title cannot be empty")
    @Size(max = 500, message = "Title cannot exceed 500 characters")
    @Schema(description = "Title of the book", example = "The Hitchhiker's Guide to the Galaxy")
    private String title;

    @NotBlank(message = "ISBN cannot be empty")
    @Size(min = 10, max = 13, message = "ISBN must be between 10 and 13 characters")
    @Schema(description = "ISBN of the book", example = "978-0345391803")
    private String isbn;

    @NotNull(message = "Publication year cannot be empty")
    @Min(value = 1000, message = "Publication year must be a valid year")
    @Schema(description = "Publication year of the book", example = "1979")
    private Integer publicationYear;

    @Schema(description = "Description or synopsis of the book", example = "A comedic science fiction series.")
    private String description;

    @Schema(description = "URL to the book's cover image", example = "http://example.com/cover.jpg")
    private String coverImageUrl;

    @NotNull(message = "Author ID cannot be empty")
    @Schema(description = "ID of the author", example = "1")
    private Long authorId;

    @Schema(description = "List of genre IDs associated with the book", example = "[1, 2]")
    private Set<Long> genreIds;
}