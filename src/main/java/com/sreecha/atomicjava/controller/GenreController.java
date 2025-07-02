package com.sreecha.atomicjava.controller;

import com.sreecha.atomicjava.dto.GenreRequest;
import com.sreecha.atomicjava.dto.GenreResponse;
import com.sreecha.atomicjava.service.GenreService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/genres")
@AllArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    public ResponseEntity<Page<GenreResponse>> getAllGenres(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<GenreResponse> genres = genreService.getAllGenres(pageable);
        return ResponseEntity.ok(genres);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreResponse> getGenreById(@PathVariable Long id) {
        GenreResponse genre = genreService.getGenreById(id);
        return ResponseEntity.ok(genre);
    }

    @PostMapping
    public ResponseEntity<GenreResponse> createGenre(@Valid @RequestBody GenreRequest genreRequest) {
        GenreResponse createdGenre = genreService.createGenre(genreRequest);
        return new ResponseEntity<>(createdGenre, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenreResponse> updateGenre(@PathVariable Long id, @Valid @RequestBody GenreRequest genreRequest) {
        GenreResponse updatedGenre = genreService.updateGenre(id, genreRequest);
        return ResponseEntity.ok(updatedGenre);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        genreService.deleteGenre(id);
        return ResponseEntity.noContent().build();
    }
}