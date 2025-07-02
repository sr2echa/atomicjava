package com.sreecha.atomicjava.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private Long id;
    private String title;
    private String content;
    private Integer rating;
    private LocalDateTime reviewDate;
    private Long userId;
    private Long bookId;
}