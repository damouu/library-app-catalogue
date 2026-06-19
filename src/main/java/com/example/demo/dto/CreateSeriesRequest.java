package com.example.demo.dto;

import java.time.LocalDate;

public record CreateSeriesRequest(
        String title,
        String genre,
        String coverArtworkUrl,
        String illustrator,
        String publisher,
        String author,
        LocalDate firstPrintPublicationDate,
        LocalDate lastPrintPublicationDate
) {
}
