package com.sreecha.atomicjava.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response DTO for Review details")
public class ReviewResponse {
    @Schema(description = "Unique identifier of the review", example = "1")
    private Long id;

    @Schema(description = "Title of the review", example = "A captivating read!")
    private String title;

    @Schema(description = "Content of the review", example = "This book was truly amazing, I couldn't put it down.")
    private String content;

    @Schema(description = "Rating given to the book (1-5 stars)", example = "5")
    private Integer rating;

    @Schema(description = "Date and time the review was submitted", example = "2023-10-26T10:00:00")
    private LocalDateTime reviewDate;

    @Schema(description = "ID of the user who submitted the review", example = "1")
    private Long userId;

    @Schema(description = "ID of the book being reviewed", example = "1")
    private Long bookId;
}