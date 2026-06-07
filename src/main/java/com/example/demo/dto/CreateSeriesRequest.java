package com.example.demo.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSeriesRequest {

    private String title;

    private String genre;

    private String coverArtworkUrl;

    private String illustrator;

    private String publisher;

    private String author;

    private LocalDate firstPrintPublicationDate;

    private LocalDate lastPrintPublicationDate;

}
