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

        return new ChapterCreatedEventData(chapter.getUuid(), chapter.getSeries().getUuid(), chapter.getTitle(), chapter.getSecondTitle(), chapter.getTotalPages(), chapter.getChapterNumber(), chapter.getSummary(), chapter.getCoverArtworkUrl(), String.valueOf(chapter.getPublicationDate()), initial_copies_count);
    }

    public Chapter toEntity(CreateChapterRequest request, Series series) {
        return Chapter.builder().uuid(UUID.randomUUID()).title(request.title()).secondTitle(request.second_title()).chapterNumber(request.chapter_number()).totalPages(request.total_pages()).coverArtworkUrl(request.cover_artwork_url()).summary(request.summary()).publicationDate(request.publication_date()).series(series).build();
    }

    public ChapterSummaryDTO toSummaryDto(Chapter chapter) {
        return new ChapterSummaryDTO(chapter.getUuid(), chapter.getTitle(), chapter.getSecondTitle(), chapter.getChapterNumber(), chapter.getTotalPages(), chapter.getSeries().getGenre(), chapter.getSummary(), chapter.getPublicationDate().toString(), chapter.getSeries().getCoverArtworkUrl(), chapter.getSeries().getUuid());
    }
}
