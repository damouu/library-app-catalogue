package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.mapper.ChapterMapper;
import com.example.demo.mapper.SeriesMapper;
import com.example.demo.model.Chapter;
import com.example.demo.model.Series;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KafkaPayloadBuilderService {

    private final SeriesMapper seriesEventMapper;

    private final ChapterMapper chapterMapper;

    public SeriesCreatedEvent seriesCreatedEvent(Series series, String eventType, String sourceService, UUID eventUUID) {

        SeriesCreatedEventData eventData = seriesEventMapper.toEventData(series);

        Metadata metadataPayload = buildMetadata(eventType, sourceService, eventUUID);

        return SeriesCreatedEvent.builder().metadata(metadataPayload).data(eventData).build();
    }

    public ChapterCreatedEvent chapterCreatedEvent(Chapter chapter, String eventType, String sourceService, UUID eventUUID, Integer initial_copies_count) {

        ChapterCreatedEventData chapterCreatedEventData = chapterMapper.toEventData(chapter, initial_copies_count);

        Metadata metadataPayload = buildMetadata(eventType, sourceService, eventUUID);

        return ChapterCreatedEvent.builder().metadata(metadataPayload).data(chapterCreatedEventData).build();
    }

    private Metadata buildMetadata(String eventType, String sourceService, UUID eventUUID) {

        return Metadata.builder().event_uuid(eventUUID).event_type(eventType).source_service(sourceService).timestamp(LocalDateTime.now().toString()).build();
    }

}