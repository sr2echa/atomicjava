package com.sreecha.atomicjava.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {
    private Long id;
    private String title;
    private String isbn;
    private Integer publicationYear;
    private String description;
    private String coverImageUrl;
    private Double averageRating;

    private AuthorResponse author;
    private Set<GenreResponse> genres;
}