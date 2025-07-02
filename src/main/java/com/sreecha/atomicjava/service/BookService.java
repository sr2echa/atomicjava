package com.sreecha.atomicjava.service;

import com.sreecha.atomicjava.dto.BookRequest;
import com.sreecha.atomicjava.dto.BookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookResponse createBook(BookRequest bookRequest);
    BookResponse getBookById(Long id);
    Page<BookResponse> getAllBooks(Pageable pageable);
    BookResponse updateBook(Long id, BookRequest bookRequest);
    void deleteBook(Long id);
}