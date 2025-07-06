package com.sreecha.atomicjava.service;

import com.sreecha.atomicjava.dto.ReviewRequest;
import com.sreecha.atomicjava.dto.ReviewResponse;
import com.sreecha.atomicjava.model.Book;
import com.sreecha.atomicjava.model.Review;
import com.sreecha.atomicjava.model.User;
import com.sreecha.atomicjava.repository.BookRepository;
import com.sreecha.atomicjava.repository.ReviewRepository;
import com.sreecha.atomicjava.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    private ReviewResponse toReviewResponse(Review review) {
        if (review == null) return null;
        return new ReviewResponse(
                review.getId(),
                review.getTitle(),
                review.getContent(),
                review.getRating(),
                review.getReviewDate(),
                review.getUser() != null ? review.getUser().getId() : null, // Get user ID
                review.getBook() != null ? review.getBook().getId() : null  // Get book ID
        );
    }

    @Override
    @Transactional
    public ReviewResponse createReview(ReviewRequest reviewRequest, Long userId) {
        Book book = bookRepository.findById(reviewRequest.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id " + reviewRequest.getBookId()));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + userId));

        Review review = new Review();
        review.setTitle(reviewRequest.getTitle());
        review.setContent(reviewRequest.getContent());
        review.setRating(reviewRequest.getRating());
        review.setReviewDate(LocalDateTime.now()); // Set current time
        review.setBook(book);
        review.setUser(user);

        Review savedReview = reviewRepository.save(review);

        // Optional: Recalculate book average rating here or in a separate step
        // book.setAverageRating(calculateAverageRating(book.getId()));
        // bookRepository.save(book);

        return toReviewResponse(savedReview);
    }

    private void updateBookAverageRating(Book book) {
        Double averageRating = reviewRepository.findByBook(book, Pageable.unpaged())
                .stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
        book.setAverageRating(averageRating);
        bookRepository.save(book);
    }

    @Override
    public ReviewResponse getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with id " + id));
        return toReviewResponse(review);
    }

    @Override
    public Page<ReviewResponse> getReviewsByBookId(Long bookId, Pageable pageable) {
        if (!bookRepository.existsById(bookId)) {
            throw new EntityNotFoundException("Book not found with id " + bookId);
        }
        return reviewRepository.findByBookId(bookId, pageable).map(this::toReviewResponse);
    }

    @Override
    public Page<ReviewResponse> getReviewsByUserId(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found with id " + userId);
        }
        return reviewRepository.findByUserId(userId, pageable).map(this::toReviewResponse);
    }


    @Override
    @Transactional
    public ReviewResponse updateReview(Long id, ReviewRequest reviewRequest, Long userId) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with id " + id));

        if (!review.getUser().getId().equals(userId)) {
            throw new SecurityException("User is not authorized to update this review.");
        }

        review.setTitle(reviewRequest.getTitle());
        review.setContent(reviewRequest.getContent());
        review.setRating(reviewRequest.getRating());
        // reviewDate usually not updated, maybe add a 'lastUpdatedDate' field if needed

        Review updatedReview = reviewRepository.save(review);
        updateBookAverageRating(review.getBook());
        return toReviewResponse(updatedReview);
    }

    @Override
    @Transactional
    public void deleteReview(Long id, Long userId) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with id " + id));

        if (!review.getUser().getId().equals(userId)) {
            throw new SecurityException("User is not authorized to delete this review.");
        }

        reviewRepository.delete(review);
        updateBookAverageRating(review.getBook());
    }
}