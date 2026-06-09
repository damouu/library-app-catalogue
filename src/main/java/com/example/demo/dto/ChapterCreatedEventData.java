package com.example.demo.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChapterCreatedEventData {

    private UUID chapter_uuid;

    private UUID series_uuid;
    
    private String title;

    private String second_title;

    private Integer total_pages;

    private Integer chapter_number;

    private String summary;

    private String cover_artwork_url;

    private String publication_date;

    private Integer initial_copies_count;

}