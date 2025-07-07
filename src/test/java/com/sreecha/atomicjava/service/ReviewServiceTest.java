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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private Review review;
    private ReviewRequest reviewRequest;
    private Book book;
    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .passwordHash("password")
                .build();

        book = Book.builder()
                .id(1L)
                .title("Test Book")
                .isbn("1234567890123")
                .build();

        review = Review.builder()
                .id(1L)
                .title("Great Book")
                .content("Loved reading this book.")
                .rating(5)
                .reviewDate(LocalDateTime.now())
                .book(book)
                .user(user)
                .build();

        reviewRequest = ReviewRequest.builder()
                .bookId(1L)
                .title("Updated Review")
                .content("It was okay.")
                .rating(3)
                .build();
    }

    @Test
    void createReview_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Review savedReview = Review.builder()
                .id(1L)
                .title(reviewRequest.getTitle())
                .content(reviewRequest.getContent())
                .rating(reviewRequest.getRating())
                .reviewDate(LocalDateTime.now())
                .book(book)
                .user(user)
                .build();

        when(reviewRepository.save(any(Review.class))).thenReturn(savedReview);

        ReviewResponse response = reviewService.createReview(reviewRequest, 1L);

        assertNotNull(response);
        assertEquals(reviewRequest.getTitle(), response.getTitle());
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void createReview_BookNotFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> reviewService.createReview(reviewRequest, 1L));
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void createReview_UserNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> reviewService.createReview(reviewRequest, 1L));
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void getReviewById_Success() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        ReviewResponse response = reviewService.getReviewById(1L);

        assertNotNull(response);
        assertEquals(review.getTitle(), response.getTitle());
    }

    @Test
    void getReviewById_NotFound() {
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> reviewService.getReviewById(1L));
    }

    @Test
    void getReviewsByBookId_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Review> reviewPage = new PageImpl<>(Arrays.asList(review), pageable, 1);
        when(bookRepository.existsById(1L)).thenReturn(true);
        when(reviewRepository.findByBookId(1L, pageable)).thenReturn(reviewPage);

        Page<ReviewResponse> responsePage = reviewService.getReviewsByBookId(1L, pageable);

        assertNotNull(responsePage);
        assertEquals(1, responsePage.getTotalElements());
        assertEquals(review.getTitle(), responsePage.getContent().get(0).getTitle());
    }

    @Test
    void getReviewsByBookId_BookNotFound() {
        when(bookRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> reviewService.getReviewsByBookId(1L, PageRequest.of(0, 10)));
    }

    @Test
    void getReviewsByUserId_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Review> reviewPage = new PageImpl<>(Arrays.asList(review), pageable, 1);
        when(userRepository.existsById(1L)).thenReturn(true);
        when(reviewRepository.findByUserId(1L, pageable)).thenReturn(reviewPage);

        Page<ReviewResponse> responsePage = reviewService.getReviewsByUserId(1L, pageable);

        assertNotNull(responsePage);
        assertEquals(1, responsePage.getTotalElements());
        assertEquals(review.getTitle(), responsePage.getContent().get(0).getTitle());
    }

    @Test
    void getReviewsByUserId_UserNotFound() {
        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> reviewService.getReviewsByUserId(1L, PageRequest.of(0, 10)));
    }

    @Test
    void updateReview_Success() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        when(reviewRepository.findByBook(any(Book.class), any(Pageable.class))).thenReturn(new PageImpl<>(Arrays.asList(review), PageRequest.of(0, 1), 1));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        ReviewResponse response = reviewService.updateReview(1L, reviewRequest, 1L);

        assertNotNull(response);
        assertEquals(reviewRequest.getTitle(), response.getTitle());
        assertEquals(reviewRequest.getContent(), response.getContent());
        assertEquals(reviewRequest.getRating(), response.getRating());
        verify(reviewRepository, times(1)).save(any(Review.class));
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void updateReview_NotFound() {
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> reviewService.updateReview(1L, reviewRequest, 1L));
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void updateReview_Unauthorized() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        assertThrows(SecurityException.class, () -> reviewService.updateReview(1L, reviewRequest, 2L)); // Different user ID
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void deleteReview_Success() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(reviewRepository.findByBook(any(Book.class), any(Pageable.class))).thenReturn(new PageImpl<>(Arrays.asList())); // No reviews left
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        assertDoesNotThrow(() -> reviewService.deleteReview(1L, 1L));
        verify(reviewRepository, times(1)).delete(any(Review.class));
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void deleteReview_NotFound() {
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> reviewService.deleteReview(1L, 1L));
        verify(reviewRepository, never()).delete(any(Review.class));
    }

    @Test
    void deleteReview_Unauthorized() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        assertThrows(SecurityException.class, () -> reviewService.deleteReview(1L, 2L)); // Different user ID
        verify(reviewRepository, never()).delete(any(Review.class));
    }
}
