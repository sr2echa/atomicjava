package com.sreecha.atomicjava.service;

import com.sreecha.atomicjava.dto.GenreRequest;
import com.sreecha.atomicjava.dto.GenreResponse;
import com.sreecha.atomicjava.model.Genre;
import com.sreecha.atomicjava.repository.GenreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    @Transactional
    public GenreResponse createGenre(GenreRequest genreRequest) {
        Genre genre = new Genre();
        genre.setName(genreRequest.getName());
        genre.setDescription(genreRequest.getDescription());
        Genre savedGenre = genreRepository.save(genre);
        return toGenreResponse(savedGenre);
    }

    @Override
    public GenreResponse getGenreById(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found with id " + id));
        return toGenreResponse(genre);
    }

    @Override
    public Page<GenreResponse> getAllGenres(Pageable pageable) {
        return genreRepository.findAll(pageable).map(this::toGenreResponse);
    }

    @Override
    public List<GenreResponse> getGenresByIds(Set<Long> ids) {
        return genreRepository.findAllById(ids)
                .stream()
                .map(this::toGenreResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public GenreResponse updateGenre(Long id, GenreRequest genreRequest) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found with id " + id));
        genre.setName(genreRequest.getName());
        genre.setDescription(genreRequest.getDescription());
        Genre updatedGenre = genreRepository.save(genre);
        return toGenreResponse(updatedGenre);
    }

    @Override
    @Transactional
    public void deleteGenre(Long id) {
        if (!genreRepository.existsById(id)) {
            throw new EntityNotFoundException("Genre not found with id " + id);
        }
        genreRepository.deleteById(id);
    }

    private GenreResponse toGenreResponse(Genre genre) {
        return new GenreResponse(
                genre.getId(),
                genre.getName(),
                genre.getDescription()
        );
    }
}