package com.sreecha.atomicjava.controller;

import com.sreecha.atomicjava.dto.ReviewRequest;
import com.sreecha.atomicjava.dto.ReviewResponse;
import com.sreecha.atomicjava.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sreecha.atomicjava.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reviews")
@AllArgsConstructor
@Tag(name = "Reviews", description = "Review management APIs")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "Create a new review",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Review created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ReviewResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid review data provided"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Book or User not found")
            })
    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(@Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Review creation request") @RequestBody ReviewRequest reviewRequest) {
        Long currentUserId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        ReviewResponse createdReview = reviewService.createReview(reviewRequest, currentUserId);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    @Operation(summary = "Get review by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved review",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ReviewResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Review not found")
            })
    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> getReviewById(
            @Parameter(description = "ID of the review to retrieve", example = "1")
            @PathVariable Long id) {
        ReviewResponse review = reviewService.getReviewById(id);
        return ResponseEntity.ok(review);
    }

    @Operation(summary = "Get reviews by book ID with pagination",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of reviews",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class))),
                    @ApiResponse(responseCode = "404", description = "Book not found")
            })
    @GetMapping("/book/{bookId}")
    public ResponseEntity<Page<ReviewResponse>> getReviewsByBookId(
            @Parameter(description = "ID of the book to retrieve reviews for", example = "1")
            @PathVariable Long bookId,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewResponse> reviews = reviewService.getReviewsByBookId(bookId, pageable);
        return ResponseEntity.ok(reviews);
    }

    @Operation(summary = "Get reviews by user ID with pagination",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of reviews",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class))),
                    @ApiResponse(responseCode = "404", description = "User not found")
            })
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<ReviewResponse>> getReviewsByUserId(
            @Parameter(description = "ID of the user to retrieve reviews for", example = "1")
            @PathVariable Long userId,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewResponse> reviews = reviewService.getReviewsByUserId(userId, pageable);
        return ResponseEntity.ok(reviews);
    }

    @Operation(summary = "Update an existing review",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Review updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ReviewResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid review data provided"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden: User not authorized to update this review"),
                    @ApiResponse(responseCode = "404", description = "Review not found")
            })
    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponse> updateReview(
            @Parameter(description = "ID of the review to update", example = "1")
            @PathVariable Long id,
            @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Review update request") @RequestBody ReviewRequest reviewRequest) {
        Long currentUserId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        ReviewResponse updatedReview = reviewService.updateReview(id, reviewRequest, currentUserId);
        return ResponseEntity.ok(updatedReview);
    }

    @Operation(summary = "Delete a review by ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Review deleted successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden: User not authorized to delete this review"),
                    @ApiResponse(responseCode = "404", description = "Review not found")
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(
            @Parameter(description = "ID of the review to delete", example = "1")
            @PathVariable Long id) {
        Long currentUserId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        reviewService.deleteReview(id, currentUserId);
        return ResponseEntity.noContent().build();
    }
}