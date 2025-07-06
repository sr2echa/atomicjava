package com.sreecha.atomicjava.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request DTO for Review creation and update")
public class ReviewRequest {
    @Schema(description = "Title of the review", example = "A captivating read!")
    private String title;

    @NotBlank(message = "Review content cannot be empty")
    @Schema(description = "Content of the review", example = "This book was truly amazing, I couldn't put it down.")
    private String content;

    @NotNull(message = "Rating cannot be empty")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot exceed 5")
    @Schema(description = "Rating given to the book (1-5 stars)", example = "5")
    private Integer rating;

    @NotNull(message = "Book ID cannot be empty")
    @Schema(description = "ID of the book being reviewed", example = "1")
    private Long bookId;

    // User ID <- authenticated user context, not from the request body
}