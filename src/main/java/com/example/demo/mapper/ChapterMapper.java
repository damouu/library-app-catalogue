package com.example.demo.mapper;

import com.example.demo.dto.ChapterCreatedEventData;
import com.example.demo.dto.ChapterSummaryDTO;
import com.example.demo.dto.CreateChapterRequest;
import com.example.demo.model.Chapter;
import com.example.demo.model.Series;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ChapterMapper {

    public ChapterCreatedEventData toEventData(Chapter chapter, Integer initial_copies_count) {

        return ChapterCreatedEventData.builder()
                .chapter_uuid(chapter.getUuid())
                .series_uuid(chapter.getSeries().getUuid())
                .title(chapter.getTitle())
                .second_title(chapter.getSecondTitle())
                .total_pages(chapter.getTotalPages())
                .chapter_number(chapter.getChapterNumber())
                .summary(chapter.getSummary())
                .cover_artwork_url(chapter.getCoverArtworkUrl())
                .publication_date(String.valueOf(chapter.getPublicationDate()))
                .initial_copies_count(initial_copies_count)
                .build();
    }

    public Chapter toEntity(CreateChapterRequest request, Series series) {
        return Chapter.builder()
                .uuid(UUID.randomUUID())
                .title(request.getTitle())
                .secondTitle(request.getSecond_title())
                .chapterNumber(request.getChapter_number())
                .totalPages(request.getTotal_pages())
                .coverArtworkUrl(request.getCover_artwork_url())
                .summary(request.getSummary())
                .publicationDate(request.getPublication_date())
                .series(series)
                .build();
    }

    public ChapterSummaryDTO toSummaryDto(Chapter chapter) {
        return ChapterSummaryDTO.builder()
                .title(chapter.getTitle())
                .genre(chapter.getSeries().getGenre())
                .build();
    }
}
