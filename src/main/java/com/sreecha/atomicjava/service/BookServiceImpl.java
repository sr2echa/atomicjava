package com.sreecha.atomicjava.service;

import com.sreecha.atomicjava.dto.BookRequest;
import com.sreecha.atomicjava.dto.BookResponse;
import com.sreecha.atomicjava.dto.AuthorResponse;
import com.sreecha.atomicjava.dto.GenreResponse;
import com.sreecha.atomicjava.model.Author;
import com.sreecha.atomicjava.model.Book;
import com.sreecha.atomicjava.model.Genre;
import com.sreecha.atomicjava.repository.AuthorRepository;
import com.sreecha.atomicjava.repository.BookRepository;
import com.sreecha.atomicjava.repository.GenreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    private AuthorResponse toAuthorResponse(Author author) {
        if (author == null) return null;
        return new AuthorResponse(author.getId(), author.getName(), author.getBiography(), author.getDob());
    }

    private GenreResponse toGenreResponse(Genre genre) {
        if (genre == null) return null;
        return new GenreResponse(genre.getId(), genre.getName(), genre.getDescription());
    }

    private BookResponse toBookResponse(Book book) {
        if (book == null) return null;

        Set<GenreResponse> genreResponses = book.getGenres() != null
                ? book.getGenres().stream()
                .map(this::toGenreResponse)
                .collect(Collectors.toSet())
                : new HashSet<>();

        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getIsbn(),
                book.getPublicationYear(),
                book.getDescription(),
                book.getCoverImageUrl(),
                book.getAverageRating(),
                toAuthorResponse(book.getAuthor()),
                genreResponses
        );
    }


    @Override
    @Transactional
    public BookResponse createBook(BookRequest bookRequest) {
        Book book = new Book();
        book.setTitle(bookRequest.getTitle());
        book.setIsbn(bookRequest.getIsbn());
        book.setPublicationYear(bookRequest.getPublicationYear());
        book.setDescription(bookRequest.getDescription());
        book.setCoverImageUrl(bookRequest.getCoverImageUrl());

        Author author = authorRepository.findById(bookRequest.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id " + bookRequest.getAuthorId()));
        book.setAuthor(author);

        if (bookRequest.getGenreIds() != null && !bookRequest.getGenreIds().isEmpty()) {
            List<Genre> genres = genreRepository.findAllById(bookRequest.getGenreIds());
            if (genres.size() != bookRequest.getGenreIds().size()) {
                Set<Long> foundIds = genres.stream().map(Genre::getId).collect(Collectors.toSet());
                Set<Long> missingIds = new HashSet<>(bookRequest.getGenreIds());
                missingIds.removeAll(foundIds);
                throw new EntityNotFoundException("Some genres not found with IDs: " + missingIds);
            }
            book.setGenres(new HashSet<>(genres));
        } else {
            book.setGenres(new HashSet<>());
        }

        Book savedBook = bookRepository.save(book);
        return toBookResponse(savedBook);
    }

    @Override
    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id " + id));
        return toBookResponse(book);
    }

    @Override
    public Page<BookResponse> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable).map(this::toBookResponse);
    }

    @Override
    @Transactional
    public BookResponse updateBook(Long id, BookRequest bookRequest) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id " + id));

        book.setTitle(bookRequest.getTitle());
        book.setIsbn(bookRequest.getIsbn());
        book.setPublicationYear(bookRequest.getPublicationYear());
        book.setDescription(bookRequest.getDescription());
        book.setCoverImageUrl(bookRequest.getCoverImageUrl());

        if (!book.getAuthor().getId().equals(bookRequest.getAuthorId())) {
            Author author = authorRepository.findById(bookRequest.getAuthorId())
                    .orElseThrow(() -> new EntityNotFoundException("Author not found with id " + bookRequest.getAuthorId()));
            book.setAuthor(author);
        }

        if (bookRequest.getGenreIds() != null) {
            List<Genre> genres = genreRepository.findAllById(bookRequest.getGenreIds());
            if (genres.size() != bookRequest.getGenreIds().size()) {
                Set<Long> foundIds = genres.stream().map(Genre::getId).collect(Collectors.toSet());
                Set<Long> missingIds = new HashSet<>(bookRequest.getGenreIds());
                missingIds.removeAll(foundIds);
                throw new EntityNotFoundException("Some genres not found with IDs: " + missingIds);
            }
            book.setGenres(new HashSet<>(genres));
        } else {
            book.setGenres(new HashSet<>());
        }


        Book updatedBook = bookRepository.save(book);
        return toBookResponse(updatedBook);
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book not found with id " + id);
        }
        bookRepository.deleteById(id);
    }
}