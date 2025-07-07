package com.sreecha.atomicjava.service;

import com.sreecha.atomicjava.dto.GenreRequest;
import com.sreecha.atomicjava.dto.GenreResponse;
import com.sreecha.atomicjava.model.Genre;
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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GenreServiceTest {

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private GenreServiceImpl genreService;

    private Genre genre;
    private GenreRequest genreRequest;

    @BeforeEach
    void setUp() {
        genre = Genre.builder()
                .id(1L)
                .name("Fiction")
                .description("Fiction books.")
                .build();

        genreRequest = GenreRequest.builder()
                .name("Fantasy")
                .description("Fantasy books.")
                .build();
    }

    @Test
    void createGenre_Success() {
        when(genreRepository.save(any(Genre.class))).thenReturn(genre);

        GenreResponse response = genreService.createGenre(genreRequest);

        assertNotNull(response);
        assertEquals(genre.getName(), response.getName());
        verify(genreRepository, times(1)).save(any(Genre.class));
    }

    @Test
    void getGenreById_Success() {
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));

        GenreResponse response = genreService.getGenreById(1L);

        assertNotNull(response);
        assertEquals(genre.getName(), response.getName());
    }

    @Test
    void getGenreById_NotFound() {
        when(genreRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> genreService.getGenreById(1L));
    }

    @Test
    void getAllGenres_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Genre> genrePage = new PageImpl<>(Arrays.asList(genre), pageable, 1);
        when(genreRepository.findAll(pageable)).thenReturn(genrePage);

        Page<GenreResponse> responsePage = genreService.getAllGenres(pageable);

        assertNotNull(responsePage);
        assertEquals(1, responsePage.getTotalElements());
        assertEquals(genre.getName(), responsePage.getContent().get(0).getName());
    }

    @Test
    void getGenresByIds_Success() {
        Set<Long> ids = new HashSet<>(Arrays.asList(1L));
        when(genreRepository.findAllById(ids)).thenReturn(Arrays.asList(genre));

        List<GenreResponse> responses = genreService.getGenresByIds(ids);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(genre.getName(), responses.get(0).getName());
    }

    @Test
    void updateGenre_Success() {
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));
        when(genreRepository.save(any(Genre.class))).thenReturn(genre);

        GenreResponse response = genreService.updateGenre(1L, genreRequest);

        assertNotNull(response);
        assertEquals(genreRequest.getName(), response.getName());
        assertEquals(genreRequest.getDescription(), response.getDescription());
        verify(genreRepository, times(1)).save(any(Genre.class));
    }

    @Test
    void updateGenre_NotFound() {
        when(genreRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> genreService.updateGenre(1L, genreRequest));
        verify(genreRepository, never()).save(any(Genre.class));
    }

    @Test
    void deleteGenre_Success() {
        when(genreRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> genreService.deleteGenre(1L));
        verify(genreRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteGenre_NotFound() {
        when(genreRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> genreService.deleteGenre(1L));
        verify(genreRepository, never()).deleteById(anyLong());
    }
}
