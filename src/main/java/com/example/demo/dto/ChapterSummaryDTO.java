package com.example.demo.dto;

import java.util.UUID;

public record ChapterSummaryDTO(
        UUID uuid,
        String title,
        String secondTitle,
        int chapterNumber,
        int totalPages,
        String genre,
        String summary,
        String publicationDate,
        String coverArtworkUrl
) {
}
