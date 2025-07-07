package com.sreecha.atomicjava.service;

import com.sreecha.atomicjava.dto.AuthorRequest;
import com.sreecha.atomicjava.dto.AuthorResponse;
import com.sreecha.atomicjava.model.Author;
import com.sreecha.atomicjava.repository.AuthorRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    private Author author;
    private AuthorRequest authorRequest;

    @BeforeEach
    void setUp() {
        author = Author.builder()
                .id(1L)
                .name("Test Author")
                .biography("A test biography.")
                .dob(LocalDate.of(1990, 1, 1))
                .build();

        authorRequest = AuthorRequest.builder()
                .name("New Author Name")
                .biography("New biography.")
                .dob(LocalDate.of(1985, 5, 10))
                .build();
    }

    @Test
    void createAuthor_Success() {
        when(authorRepository.save(any(Author.class))).thenReturn(author);

        AuthorResponse response = authorService.createAuthor(authorRequest);

        assertNotNull(response);
        assertEquals(author.getName(), response.getName());
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    void getAuthorById_Success() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        AuthorResponse response = authorService.getAuthorById(1L);

        assertNotNull(response);
        assertEquals(author.getName(), response.getName());
    }

    @Test
    void getAuthorById_NotFound() {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> authorService.getAuthorById(1L));
    }

    @Test
    void getAllAuthors_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Author> authorPage = new PageImpl<>(Arrays.asList(author), pageable, 1);
        when(authorRepository.findAll(pageable)).thenReturn(authorPage);

        Page<AuthorResponse> responsePage = authorService.getAllAuthors(pageable);

        assertNotNull(responsePage);
        assertEquals(1, responsePage.getTotalElements());
        assertEquals(author.getName(), responsePage.getContent().get(0).getName());
    }

    @Test
    void updateAuthor_Success() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(authorRepository.save(any(Author.class))).thenReturn(author);

        AuthorResponse response = authorService.updateAuthor(1L, authorRequest);

        assertNotNull(response);
        assertEquals(authorRequest.getName(), response.getName());
        assertEquals(authorRequest.getBiography(), response.getBiography());
        assertEquals(authorRequest.getDob(), response.getDob());
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    void updateAuthor_NotFound() {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> authorService.updateAuthor(1L, authorRequest));
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    void deleteAuthor_Success() {
        when(authorRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> authorService.deleteAuthor(1L));
        verify(authorRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteAuthor_NotFound() {
        when(authorRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> authorService.deleteAuthor(1L));
        verify(authorRepository, never()).deleteById(anyLong());
    }
}
