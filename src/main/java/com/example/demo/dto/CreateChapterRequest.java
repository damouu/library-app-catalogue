package com.example.demo.dto;

import java.time.LocalDate;
import java.util.UUID;


public record CreateChapterRequest(
        UUID series_uuid,
        String title,
        String second_title,
        Integer chapter_number,
        Integer total_pages,
        String cover_artwork_url,
        String summary,
        Integer initial_copies_count,
        LocalDate publication_date
) {
}
