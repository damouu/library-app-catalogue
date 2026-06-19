package com.example.demo.dto;

import java.util.UUID;


public record SeriesCreatedEventData(
        UUID series_uuid,
        String title,
        String genre,
        String author,
        String illustrator,
        String publisher,
        String cover_artwork_url,
        String first_print_publication_date,
        String last_print_publication_date
) {
}