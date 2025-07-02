package com.sreecha.atomicjava.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sreecha.atomicjava.model.Book;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbn(String isbn);
    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Book> findByAuthorNameContainingIgnoreCase(String authorName, Pageable pageable);
    Page<Book> findByTitleContainingIgnoreCaseOrAuthorNameContainingIgnoreCase(String title, String authorName, Pageable pageable);
    Page<Book> findByGenresNameContainingIgnoreCase(String genreName, Pageable pageable);
}