package com.example.demo.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public record ChapterSummaryDTO(
        UUID uuid,
        String title,
        String secondTitle,
        int chapterNumber,
        int totalPages,
        String genre,
        String summary,
        LocalDate publicationDate,
        String coverArtworkUrl,
        SeriesSummaryDTO series
) implements Serializable {
}
