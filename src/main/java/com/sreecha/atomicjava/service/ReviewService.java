package com.sreecha.atomicjava.service;

import com.sreecha.atomicjava.dto.ReviewRequest;
import com.sreecha.atomicjava.dto.ReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {
    ReviewResponse createReview(ReviewRequest reviewRequest, Long userId);
    ReviewResponse getReviewById(Long id);
    Page<ReviewResponse> getReviewsByBookId(Long bookId, Pageable pageable);
    Page<ReviewResponse> getReviewsByUserId(Long userId, Pageable pageable);
    ReviewResponse updateReview(Long id, ReviewRequest reviewRequest, Long userId);
    void deleteReview(Long id, Long userId);
}