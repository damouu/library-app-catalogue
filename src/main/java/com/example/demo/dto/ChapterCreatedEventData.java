package com.example.demo.dto;

import java.util.UUID;


public record ChapterCreatedEventData(
        UUID chapter_uuid,
        UUID series_uuid,
        String title,
        String second_title,
        Integer total_pages,
        Integer chapter_number,
        String summary,
        String cover_artwork_url,
        String publication_date,
        Integer initial_copies_count
) {
}