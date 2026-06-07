package com.example.demo.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeriesCreatedEventData {

    private UUID series_uuid;

    private String title;

    private String genre;

    private String author;

    private String illustrator;

    private String publisher;

    private String cover_artwork_url;

    private String first_print_publication_date;

    private String last_print_publication_date;

}