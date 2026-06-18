package com.example.demo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChapterSummaryDTO {

    private String title;

    private String genre;

    private String coverArtworkUrl;
}
