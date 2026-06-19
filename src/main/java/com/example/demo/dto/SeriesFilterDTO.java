package com.example.demo.dto;

public record SeriesFilterDTO(
        String title,
        String author,
        String illustrator,
        String genre,
        String publisher
) {
}