package com.sreecha.atomicjava.controller;

import com.sreecha.atomicjava.dto.BookRequest;
import com.sreecha.atomicjava.dto.BookResponse;
import com.sreecha.atomicjava.service.BookService;
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
@RequestMapping("/api/books")
@AllArgsConstructor
@Tag(name = "Books", description = "Book management APIs")
public class BookController {

    private final BookService bookService;

    @Operation(summary = "Get all books with pagination and optional filtering",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of books",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class)))
            })
    @GetMapping
    public ResponseEntity<Page<BookResponse>> getAllBooks(
            @Parameter(example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(example = "Lord of the Rings")
            @RequestParam(required = false) String query,
            @Parameter(example = "J.R.R. Tolkien")
            @RequestParam(required = false) String authorName,
            @Parameter(example = "Fantasy")
            @RequestParam(required = false) String genreName) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BookResponse> books = bookService.searchBooks(query, authorName, genreName, pageable);
        return ResponseEntity.ok(books);
    }

    @Operation(summary = "Get book by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved book",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Book not found")
            })
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(
            @Parameter(example = "1")
            @PathVariable Long id) {
        BookResponse book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    @Operation(summary = "Create a new book",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Book created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid book data provided")
            })
    @PostMapping
    public ResponseEntity<BookResponse> createBook(@Valid @io.swagger.v3.oas.annotations.parameters.RequestBody @RequestBody BookRequest bookRequest) {
        BookResponse createdBook = bookService.createBook(bookRequest);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing book",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Book updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid book data provided"),
                    @ApiResponse(responseCode = "404", description = "Book not found")
            })
    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(
            @Parameter(example = "1")
            @PathVariable Long id,
            @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody @RequestBody BookRequest bookRequest) {
        BookResponse updatedBook = bookService.updateBook(id, bookRequest);
        return ResponseEntity.ok(updatedBook);
    }

    @Operation(summary = "Delete a book by ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Book not found")
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(
            @Parameter(example = "1")
            @PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}