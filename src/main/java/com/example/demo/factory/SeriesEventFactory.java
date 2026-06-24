package com.example.demo.factory;

import com.example.demo.dto.Metadata;
import com.example.demo.dto.SeriesCreatedEvent;
import com.example.demo.dto.SeriesCreatedEventData;
import com.example.demo.mapper.SeriesMapper;
import com.example.demo.model.Series;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SeriesEventFactory {

    private final SeriesMapper seriesEventMapper;

    public SeriesCreatedEvent seriesCreatedEvent(Series series, String eventType, String sourceService, UUID eventUUID) {
        SeriesCreatedEventData eventData = seriesEventMapper.toEventData(series);
        Metadata metadata = new Metadata(LocalDateTime.now().toString(), sourceService, eventType, eventUUID);
        return new SeriesCreatedEvent(metadata, eventData);
    }
}
