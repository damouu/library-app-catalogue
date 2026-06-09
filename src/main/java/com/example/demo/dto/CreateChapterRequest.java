package com.example.demo.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateChapterRequest {

    private UUID series_uuid;

    private String title;

    private String second_title;

    private Integer chapter_number;

    private Integer total_pages;

    private String cover_artwork_url;

    private String summary;

    private Integer initial_copies_count;

    private LocalDate publication_date;
}
