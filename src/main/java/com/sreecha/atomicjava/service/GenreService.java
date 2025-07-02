package com.sreecha.atomicjava.service;

import com.sreecha.atomicjava.dto.GenreRequest;
import com.sreecha.atomicjava.dto.GenreResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface GenreService {
    GenreResponse createGenre(GenreRequest genreRequest);
    GenreResponse getGenreById(Long id);
    Page<GenreResponse> getAllGenres(Pageable pageable);
    List<GenreResponse> getGenresByIds(Set<Long> ids); // For getting multiple genres by IDs
    GenreResponse updateGenre(Long id, GenreRequest genreRequest);
    void deleteGenre(Long id);
}