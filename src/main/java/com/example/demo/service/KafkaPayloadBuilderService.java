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
        Metadata metadata = new Metadata(LocalDateTime.now().toString(), sourceService, eventType, eventUUID);
        return new SeriesCreatedEvent(metadata, eventData);
    }

    public ChapterCreatedEvent chapterCreatedEvent(Chapter chapter, String eventType, String sourceService, UUID eventUUID, Integer initial_copies_count) {
        ChapterCreatedEventData chapterCreatedEventData = chapterMapper.toEventData(chapter, initial_copies_count);
        Metadata metadata = new Metadata(LocalDateTime.now().toString(), sourceService, eventType, eventUUID);
        return new ChapterCreatedEvent(metadata, chapterCreatedEventData);
    }

}