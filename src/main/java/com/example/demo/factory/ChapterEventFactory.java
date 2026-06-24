package com.example.demo.factory;

import com.example.demo.dto.ChapterCreatedEvent;
import com.example.demo.dto.ChapterCreatedEventData;
import com.example.demo.dto.Metadata;
import com.example.demo.mapper.ChapterMapper;
import com.example.demo.model.Chapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChapterEventFactory {

    private final ChapterMapper chapterMapper;

    public ChapterCreatedEvent chapterCreatedEvent(Chapter chapter, String eventType, String sourceService, UUID eventUUID, Integer initial_copies_count) {
        ChapterCreatedEventData chapterCreatedEventData = chapterMapper.toEventData(chapter, initial_copies_count);
        Metadata metadata = new Metadata(LocalDateTime.now().toString(), sourceService, eventType, eventUUID);
        return new ChapterCreatedEvent(metadata, chapterCreatedEventData);
    }

}
