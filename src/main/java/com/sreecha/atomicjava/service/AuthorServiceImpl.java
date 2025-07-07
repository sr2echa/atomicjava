package com.sreecha.atomicjava.service;

import com.sreecha.atomicjava.dto.AuthorRequest;
import com.sreecha.atomicjava.dto.AuthorResponse;
import com.sreecha.atomicjava.model.Author;
import com.sreecha.atomicjava.repository.AuthorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    @Transactional
    public AuthorResponse createAuthor(AuthorRequest authorRequest) {
        Author author = new Author();
        author.setName(authorRequest.getName());
        author.setBiography(authorRequest.getBiography());
        author.setDob(authorRequest.getDob());
        Author savedAuthor = authorRepository.save(author);
        return toAuthorResponse(savedAuthor);
    }

    @Override
    public AuthorResponse getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id " + id));
        return toAuthorResponse(author);
    }

    @Override
    public Page<AuthorResponse> getAllAuthors(Pageable pageable) {
        return authorRepository.findAll(pageable).map(this::toAuthorResponse);
    }

    @Override
    @Transactional
    public AuthorResponse updateAuthor(Long id, AuthorRequest authorRequest) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id " + id));
        author.setName(authorRequest.getName());
        author.setBiography(authorRequest.getBiography());
        author.setDob(authorRequest.getDob());
        Author updatedAuthor = authorRepository.save(author);
        return toAuthorResponse(updatedAuthor);
    }

    @Override
    @Transactional
    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new EntityNotFoundException("Author not found with id " + id);
        }
        authorRepository.deleteById(id);
    }

    private AuthorResponse toAuthorResponse(Author author) {
        return new AuthorResponse(
                author.getId(),
                author.getName(),
                author.getBiography(),
                author.getDob()
        );
    }
}