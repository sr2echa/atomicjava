package com.sreecha.atomicjava.service;

import com.sreecha.atomicjava.dto.BookRequest;
import com.sreecha.atomicjava.dto.BookResponse;
import com.sreecha.atomicjava.model.Author;
import com.sreecha.atomicjava.model.Book;
import com.sreecha.atomicjava.model.Genre;
import com.sreecha.atomicjava.repository.AuthorRepository;
import com.sreecha.atomicjava.repository.BookRepository;
import com.sreecha.atomicjava.repository.GenreRepository;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;
    private Author author;
    private Genre genre1, genre2;
    private BookRequest bookRequest;

    @BeforeEach
    void setUp() {
        author = Author.builder()
                .id(1L)
                .name("Test Author")
                .dob(LocalDate.of(1900, 1, 1))
                .build();

        genre1 = Genre.builder().id(1L).name("Fiction").build();
        genre2 = Genre.builder().id(2L).name("Fantasy").build();

        book = Book.builder()
                .id(1L)
                .title("Test Book")
                .isbn("1234567890123")
                .publicationYear(2000)
                .description("A test book")
                .coverImageUrl("http://example.com/cover.jpg")
                .averageRating(4.5)
                .author(author)
                .genres(new HashSet<>(Arrays.asList(genre1, genre2)))
                .build();

        bookRequest = BookRequest.builder()
                .title("New Test Book")
                .isbn("9876543210987")
                .publicationYear(2022)
                .description("A new test book")
                .coverImageUrl("http://example.com/new_cover.jpg")
                .authorId(1L)
                .genreIds(new HashSet<>(Arrays.asList(1L, 2L)))
                .build();
    }

    @Test
    void createBook_Success() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(genreRepository.findAllById(anyIterable())).thenReturn(Arrays.asList(genre1, genre2));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookResponse response = bookService.createBook(bookRequest);

        assertNotNull(response);
        assertEquals(bookRequest.getTitle(), response.getTitle());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void createBook_AuthorNotFound() {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.createBook(bookRequest));
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void createBook_SomeGenresNotFound() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(genreRepository.findAllById(anyIterable())).thenReturn(Arrays.asList(genre1));

        assertThrows(EntityNotFoundException.class, () -> bookService.createBook(bookRequest));
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void getBookById_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookResponse response = bookService.getBookById(1L);

        assertNotNull(response);
        assertEquals(book.getTitle(), response.getTitle());
    }

    @Test
    void getAllBooks_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(Arrays.asList(book), pageable, 1);
        when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        Page<BookResponse> responsePage = bookService.getAllBooks(pageable);

        assertNotNull(responsePage);
        assertEquals(1, responsePage.getTotalElements());
        assertEquals(book.getTitle(), responsePage.getContent().get(0).getTitle());
    }

    @Test
    void updateBook_Success() {
        Book existingBook = Book.builder()
                .id(1L)
                .title("Original Title")
                .isbn("original-isbn")
                .publicationYear(2000)
                .description("Original description")
                .coverImageUrl("original.jpg")
                .averageRating(4.0)
                .author(author)
                .genres(new HashSet<>(Arrays.asList(genre1)))
                .build();

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(genreRepository.findAllById(anyIterable())).thenReturn(Arrays.asList(genre1, genre2));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookResponse response = bookService.updateBook(1L, bookRequest);

        assertNotNull(response);
        assertEquals(bookRequest.getTitle(), response.getTitle());
        assertEquals(bookRequest.getIsbn(), response.getIsbn());
        assertEquals(bookRequest.getPublicationYear(), response.getPublicationYear());
        assertEquals(bookRequest.getDescription(), response.getDescription());
        assertEquals(bookRequest.getCoverImageUrl(), response.getCoverImageUrl());
        assertEquals(bookRequest.getAuthorId(), response.getAuthor().getId());
        assertEquals(bookRequest.getGenreIds().size(), response.getGenres().size());

        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void updateBook_NotFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.updateBook(1L, bookRequest));
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void deleteBook_Success() {
        when(bookRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> bookService.deleteBook(1L));
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteBook_NotFound() {
        when(bookRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> bookService.deleteBook(1L));
        verify(bookRepository, never()).deleteById(anyLong());
    }

    @Test
    void searchBooks_ByQuery() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(Arrays.asList(book), pageable, 1);
        when(bookRepository.findByTitleContainingIgnoreCaseOrAuthorNameContainingIgnoreCase(anyString(), anyString(), any(Pageable.class)))
                .thenReturn(bookPage);

        Page<BookResponse> responsePage = bookService.searchBooks("Test", null, null, pageable);

        assertNotNull(responsePage);
        assertEquals(1, responsePage.getTotalElements());
        assertEquals(book.getTitle(), responsePage.getContent().get(0).getTitle());
        verify(bookRepository, times(1)).findByTitleContainingIgnoreCaseOrAuthorNameContainingIgnoreCase(anyString(), anyString(), any(Pageable.class));
    }

    @Test
    void searchBooks_ByAuthorName() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(Arrays.asList(book), pageable, 1);
        when(bookRepository.findByAuthorNameContainingIgnoreCase(anyString(), any(Pageable.class)))
                .thenReturn(bookPage);

        Page<BookResponse> responsePage = bookService.searchBooks(null, "Test Author", null, pageable);

        assertNotNull(responsePage);
        assertEquals(1, responsePage.getTotalElements());
        assertEquals(book.getTitle(), responsePage.getContent().get(0).getTitle());
        verify(bookRepository, times(1)).findByAuthorNameContainingIgnoreCase(anyString(), any(Pageable.class));
    }

    @Test
    void searchBooks_ByGenreName() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(Arrays.asList(book), pageable, 1);
        when(bookRepository.findByGenresNameContainingIgnoreCase(anyString(), any(Pageable.class)))
                .thenReturn(bookPage);

        Page<BookResponse> responsePage = bookService.searchBooks(null, null, "Fiction", pageable);

        assertNotNull(responsePage);
        assertEquals(1, responsePage.getTotalElements());
        assertEquals(book.getTitle(), responsePage.getContent().get(0).getTitle());
        verify(bookRepository, times(1)).findByGenresNameContainingIgnoreCase(anyString(), any(Pageable.class));
    }

    @Test
    void searchBooks_NoCriteria() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(Arrays.asList(book), pageable, 1);
        when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        Page<BookResponse> responsePage = bookService.searchBooks(null, null, null, pageable);

        assertNotNull(responsePage);
        assertEquals(1, responsePage.getTotalElements());
        assertEquals(book.getTitle(), responsePage.getContent().get(0).getTitle());
        verify(bookRepository, times(1)).findAll(pageable);
    }
}