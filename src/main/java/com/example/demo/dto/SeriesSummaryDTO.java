package com.example.demo.dto;

import java.util.UUID;

public record SeriesSummaryDTO(
        UUID series_uuid,
        String title,
        String genre,
        String coverArtworkUrl,
        String author,
        String illustration,
        String publisher
) {
}
