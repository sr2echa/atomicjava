package com.sreecha.atomicjava.service;

import com.sreecha.atomicjava.dto.AuthorRequest;
import com.sreecha.atomicjava.dto.AuthorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthorService {
    AuthorResponse createAuthor(AuthorRequest authorRequest);
    AuthorResponse getAuthorById(Long id);
    Page<AuthorResponse> getAllAuthors(Pageable pageable);
    AuthorResponse updateAuthor(Long id, AuthorRequest authorRequest);
    void deleteAuthor(Long id);
    
}