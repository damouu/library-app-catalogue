package com.example.demo.service;

import com.example.demo.dto.ChapterCreatedEvent;
import com.example.demo.dto.SeriesCreatedEvent;
import com.example.demo.event.publish.CatalogueEventPublisher;
import com.example.demo.factory.ChapterEventFactory;
import com.example.demo.factory.SeriesEventFactory;
import com.example.demo.model.Chapter;
import com.example.demo.model.Series;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CatalogueEventPublisherTest {

    @Mock
    private SeriesEventFactory seriesEventFactory;

    @Mock
    private ChapterEventFactory chapterEventFactory;

    @Mock
    private KafkaTemplate<UUID, Object> kafkaTemplate;

    @InjectMocks
    private CatalogueEventPublisher catalogueEventPublisher;

    @Test
    void shouldPublishChapterCreatedEvent() {
        Chapter chapter = Instancio.create(Chapter.class);
        ChapterCreatedEvent event = Instancio.create(ChapterCreatedEvent.class);
        when(chapterEventFactory.chapterCreatedEvent(eq(chapter), eq("CHAPTER_CREATED"), eq("library-app-catalogue-v2"), any(UUID.class), eq(1))).thenReturn(event);
        catalogueEventPublisher.publishChapterCreated(chapter, 1);
        verify(chapterEventFactory).chapterCreatedEvent(eq(chapter), eq("CHAPTER_CREATED"), eq("library-app-catalogue-v2"), any(UUID.class), eq(1));
        verify(kafkaTemplate).send(eq("library.catalog.v1"), any(UUID.class), eq(event));
    }

    @Test
    void shouldPublishSeriesCreatedEvent() {
        Series series = Instancio.create(Series.class);
        SeriesCreatedEvent event = Instancio.create(SeriesCreatedEvent.class);
        when(seriesEventFactory.seriesCreatedEvent(eq(series), eq("SERIES_CREATED"), eq("library-app-catalogue-v2"), any(UUID.class))).thenReturn(event);
        catalogueEventPublisher.publishSeriesCreated(series);
        verify(seriesEventFactory).seriesCreatedEvent(eq(series), eq("SERIES_CREATED"), eq("library-app-catalogue-v2"), any(UUID.class));
        verify(kafkaTemplate).send(eq("library.catalog.v1"), any(UUID.class), eq(event));
    }
}