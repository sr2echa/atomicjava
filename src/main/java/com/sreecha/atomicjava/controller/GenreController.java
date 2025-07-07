package com.sreecha.atomicjava.controller;

import com.sreecha.atomicjava.dto.GenreRequest;
import com.sreecha.atomicjava.dto.GenreResponse;
import com.sreecha.atomicjava.service.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Genres", description = "Genre management APIs")
public class GenreController {

    private final GenreService genreService;

    @Operation(summary = "Get all genres with pagination",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of genres",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class)))
            })
    @GetMapping
    public ResponseEntity<Page<GenreResponse>> getAllGenres(
            @Parameter(example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(example = "10")
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<GenreResponse> genres = genreService.getAllGenres(pageable);
        return ResponseEntity.ok(genres);
    }

    @Operation(summary = "Get genre by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved genre",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = GenreResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Genre not found")
            })
    @GetMapping("/{id}")
    public ResponseEntity<GenreResponse> getGenreById(
            @Parameter(example = "1")
            @PathVariable Long id) {
        GenreResponse genre = genreService.getGenreById(id);
        return ResponseEntity.ok(genre);
    }

    @Operation(summary = "Create a new genre",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Genre created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = GenreResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid genre data provided")
            })
    @PostMapping
    public ResponseEntity<GenreResponse> createGenre(@Valid @io.swagger.v3.oas.annotations.parameters.RequestBody @RequestBody GenreRequest genreRequest) {
        GenreResponse createdGenre = genreService.createGenre(genreRequest);
        return new ResponseEntity<>(createdGenre, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing genre",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Genre updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = GenreResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid genre data provided"),
                    @ApiResponse(responseCode = "404", description = "Genre not found")
            })
    @PutMapping("/{id}")
    public ResponseEntity<GenreResponse> updateGenre(
            @Parameter(example = "1")
            @PathVariable Long id,
            @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody @RequestBody GenreRequest genreRequest) {
        GenreResponse updatedGenre = genreService.updateGenre(id, genreRequest);
        return ResponseEntity.ok(updatedGenre);
    }

    @Operation(summary = "Delete a genre by ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Genre deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Genre not found")
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(
            @Parameter(example = "1")
            @PathVariable Long id) {
        genreService.deleteGenre(id);
        return ResponseEntity.noContent().build();
    }
}