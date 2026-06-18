package com.example.demo.service;

import com.example.demo.dto.ChapterCreatedEvent;
import com.example.demo.dto.SeriesCreatedEvent;
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
    private KafkaPayloadBuilderService kafkaPayloadBuilderService;

    @Mock
    private KafkaTemplate<UUID, Object> kafkaTemplate;

    @InjectMocks
    private CatalogueEventPublisher catalogueEventPublisher;

    @Test
    void shouldPublishChapterCreatedEvent() {
        Chapter chapter = Instancio.create(Chapter.class);
        ChapterCreatedEvent event = Instancio.create(ChapterCreatedEvent.class);
        when(kafkaPayloadBuilderService.chapterCreatedEvent(eq(chapter), eq("CHAPTER_CREATED"), eq("library-app-catalogue-v1"), any(UUID.class), eq(1))).thenReturn(event);
        catalogueEventPublisher.publishChapterCreated(chapter, 1);
        verify(kafkaPayloadBuilderService).chapterCreatedEvent(eq(chapter), eq("CHAPTER_CREATED"), eq("library-app-catalogue-v1"), any(UUID.class), eq(1));
        verify(kafkaTemplate).send(eq("library.catalog.v1"), any(UUID.class), eq(event));
    }

    @Test
    void shouldPublishSeriesCreatedEvent() {
        Series series = Instancio.create(Series.class);
        SeriesCreatedEvent event = Instancio.create(SeriesCreatedEvent.class);
        when(kafkaPayloadBuilderService.seriesCreatedEvent(eq(series), eq("SERIES_CREATED"), eq("library-app-catalogue-v1"), any(UUID.class))).thenReturn(event);
        catalogueEventPublisher.publishSeriesCreated(series);
        verify(kafkaPayloadBuilderService).seriesCreatedEvent(eq(series), eq("SERIES_CREATED"), eq("library-app-catalogue-v1"), any(UUID.class));
        verify(kafkaTemplate).send(eq("library.catalog.v1"), any(UUID.class), eq(event));
    }
}