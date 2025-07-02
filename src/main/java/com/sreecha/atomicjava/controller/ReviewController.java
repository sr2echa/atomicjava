package com.sreecha.atomicjava.controller;

import com.sreecha.atomicjava.dto.ReviewRequest;
import com.sreecha.atomicjava.dto.ReviewResponse;
import com.sreecha.atomicjava.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reviews")
@AllArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(@Valid @RequestBody ReviewRequest reviewRequest) {
        Long currentUserId = 1L; // Placeholder for authenticated user ID
        ReviewResponse createdReview = reviewService.createReview(reviewRequest, currentUserId);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> getReviewById(@PathVariable Long id) {
        ReviewResponse review = reviewService.getReviewById(id);
        return ResponseEntity.ok(review);
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<Page<ReviewResponse>> getReviewsByBookId(
            @PathVariable Long bookId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewResponse> reviews = reviewService.getReviewsByBookId(bookId, pageable);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<ReviewResponse>> getReviewsByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewResponse> reviews = reviewService.getReviewsByUserId(userId, pageable);
        return ResponseEntity.ok(reviews);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponse> updateReview(@PathVariable Long id, @Valid @RequestBody ReviewRequest reviewRequest) {
        Long currentUserId = 1L; // placeholder for authenticated user ID
        ReviewResponse updatedReview = reviewService.updateReview(id, reviewRequest, currentUserId);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        Long currentUserId = 1L; // Placeholder for authenticated user ID
        reviewService.deleteReview(id, currentUserId);
        return ResponseEntity.noContent().build();
    }
}