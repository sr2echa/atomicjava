package com.sreecha.atomicjava.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response DTO for Book details")
public class BookResponse {
    @Schema(description = "Unique identifier of the book", example = "1")
    private Long id;

    @Schema(description = "Title of the book", example = "The Hitchhiker's Guide to the Galaxy")
    private String title;

    @Schema(description = "ISBN of the book", example = "978-0345391803")
    private String isbn;

    @Schema(description = "Publication year of the book", example = "1979")
    private Integer publicationYear;

    @Schema(description = "Description or synopsis of the book", example = "A comedic science fiction series.")
    private String description;

    @Schema(description = "URL to the book's cover image", example = "http://example.com/cover.jpg")
    private String coverImageUrl;

    @Schema(description = "Average rating of the book based on reviews", example = "4.2")
    private Double averageRating;

    @Schema(description = "Author details")
    private AuthorResponse author;

    @Schema(description = "Genres associated with the book")
    private Set<GenreResponse> genres;
}