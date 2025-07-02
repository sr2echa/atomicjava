package com.sreecha.atomicjava.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sreecha.atomicjava.model.Review;
import com.sreecha.atomicjava.model.User;
import com.sreecha.atomicjava.model.Book;

import java.util.List;
import java.util.Optional;


@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByBook(Book book, Pageable pageable);
    Page<Review> findByUser(User user, Pageable pageable);
    Optional<Review> findByBookAndUser(Book book, User user);
    Page<Review> findByBookId(Long bookId, Pageable pageable);
    Page<Review> findByUserId(Long userId, Pageable pageable);
}