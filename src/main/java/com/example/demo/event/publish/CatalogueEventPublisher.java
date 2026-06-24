package com.example.demo.event.publish;

import com.example.demo.dto.ChapterCreatedEvent;
import com.example.demo.dto.SeriesCreatedEvent;
import com.example.demo.factory.ChapterEventFactory;
import com.example.demo.factory.SeriesEventFactory;
import com.example.demo.model.Chapter;
import com.example.demo.model.Series;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogueEventPublisher implements CatalogueEventPort {

    private final KafkaTemplate<UUID, Object> kafkaTemplate;

    private final ChapterEventFactory chapterEventFactory;

    private final SeriesEventFactory seriesEventFactory;

    public void publishChapterCreated(Chapter chapter, int initial_copies_count) {
        UUID eventUUID = UUID.randomUUID();
        log.info("Publishing CHAPTER_CREATED chapter {}", chapter.getUuid());
        ChapterCreatedEvent chapterCreatedEvent = chapterEventFactory.chapterCreatedEvent(chapter, "CHAPTER_CREATED", "library-app-catalogue-v2", eventUUID, initial_copies_count);
        kafkaTemplate.send("library.catalog.v1", eventUUID, chapterCreatedEvent);
        log.info("Published BORROW_CREATED event {}", chapter.getUuid());
    }

    public void publishSeriesCreated(Series series) {
        UUID eventUUID = UUID.randomUUID();
        log.info("Published SERIES_CREATED event {}", series.getUuid());
        SeriesCreatedEvent seriesCreatedEvent = seriesEventFactory.seriesCreatedEvent(series, "SERIES_CREATED", "library-app-catalogue-v2", eventUUID);
        kafkaTemplate.send("library.catalog.v1", eventUUID, seriesCreatedEvent);
        log.info("Published SERIES_CREATED event {}", series.getUuid());
    }


}
