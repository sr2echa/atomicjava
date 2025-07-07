package com.sreecha.atomicjava.controller;

import com.sreecha.atomicjava.dto.AuthorRequest;
import com.sreecha.atomicjava.dto.AuthorResponse;
import com.sreecha.atomicjava.service.AuthorService;
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
@RequestMapping("/api/authors")
@AllArgsConstructor
@Tag(name = "Authors", description = "Author management APIs")
public class AuthorController {

    private final AuthorService authorService;

    @Operation(summary = "Get all authors with pagination",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of authors",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class)))
            })
    @GetMapping
    public ResponseEntity<Page<AuthorResponse>> getAllAuthors(
            @Parameter(example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(example = "10")
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AuthorResponse> authors = authorService.getAllAuthors(pageable);
        return ResponseEntity.ok(authors);
    }

    @Operation(summary = "Get author by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved author",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AuthorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Author not found")
            })
    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponse> getAuthorById(
            @Parameter(example = "1")
            @PathVariable Long id) {
        AuthorResponse author = authorService.getAuthorById(id);
        return ResponseEntity.ok(author);
    }

    @Operation(summary = "Create a new author",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Author created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AuthorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid author data provided")
            })
    @PostMapping
    public ResponseEntity<AuthorResponse> createAuthor(@Valid @io.swagger.v3.oas.annotations.parameters.RequestBody @RequestBody AuthorRequest authorRequest) {
        AuthorResponse createdAuthor = authorService.createAuthor(authorRequest);
        return new ResponseEntity<>(createdAuthor, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing author",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Author updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AuthorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid author data provided"),
                    @ApiResponse(responseCode = "404", description = "Author not found")
            })
    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponse> updateAuthor(
            @Parameter(example = "1")
            @PathVariable Long id,
            @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody @RequestBody AuthorRequest authorRequest) {
        AuthorResponse updatedAuthor = authorService.updateAuthor(id, authorRequest);
        return ResponseEntity.ok(updatedAuthor);
    }

    @Operation(summary = "Delete an author by ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Author deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Author not found")
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(
            @Parameter(example = "1")
            @PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
}