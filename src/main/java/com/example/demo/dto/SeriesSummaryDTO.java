package com.example.demo.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public record SeriesSummaryDTO(
        UUID series_uuid,
        String title,
        String genre,
        String coverArtworkUrl,
        String author,
        String illustration,
        String publisher,
        LocalDate firstPrintPublicationDate,
        LocalDate lastPrintPublicationDate
) implements Serializable {
}
