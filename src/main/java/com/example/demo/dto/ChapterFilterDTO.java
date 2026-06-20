package com.example.demo.dto;

import java.time.LocalDate;

public record ChapterFilterDTO(
        String type,
        String title,
        Integer chapterNumber,
        String secondTitle,
        LocalDate publicationDate
) {
}
