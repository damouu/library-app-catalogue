package com.example.demo.service;

import com.example.demo.dto.ChapterCreatedEvent;
import com.example.demo.model.Chapter;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChapterEventPublisher {

    private final KafkaTemplate<UUID, Object> kafkaTemplate;

    private final KafkaPayloadBuilderService kafkaPayloadBuilderService;

    public void publishChapterCreated(Chapter chapter, int initial_copies_count) {
        UUID eventUUID = UUID.randomUUID();
        ChapterCreatedEvent chapterCreatedEvent = kafkaPayloadBuilderService.chapterCreatedEvent(chapter, "CHAPTER_CREATED", "library-app-catalogue-v1", eventUUID, initial_copies_count);
        kafkaTemplate.send("library.catalog.v1", eventUUID, chapterCreatedEvent);
    }


}
