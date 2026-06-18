package com.example.demo.service;

import com.example.demo.dto.ChapterCreatedEvent;
import com.example.demo.dto.SeriesCreatedEvent;
import com.example.demo.model.Chapter;
import com.example.demo.model.Series;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CatalogueEventPublisher {

    private final KafkaTemplate<UUID, Object> kafkaTemplate;

    private final KafkaPayloadBuilderService kafkaPayloadBuilderService;

    public void publishChapterCreated(Chapter chapter, int initial_copies_count) {
        UUID eventUUID = UUID.randomUUID();
        ChapterCreatedEvent chapterCreatedEvent = kafkaPayloadBuilderService.chapterCreatedEvent(chapter, "CHAPTER_CREATED", "library-app-catalogue-v1", eventUUID, initial_copies_count);
        kafkaTemplate.send("library.catalog.v1", eventUUID, chapterCreatedEvent);
    }

    public void publishSeriesCreated(Series series) {
        UUID eventUUID = UUID.randomUUID();
        SeriesCreatedEvent seriesCreatedEvent = kafkaPayloadBuilderService.seriesCreatedEvent(series, "SERIES_CREATED", "library-app-catalogue-v1", eventUUID);
        kafkaTemplate.send("library.catalog.v1", eventUUID, seriesCreatedEvent);
    }


}
