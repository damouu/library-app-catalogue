package com.example.demo.service;

import com.example.demo.dto.Metadata;
import com.example.demo.dto.SeriesCreatedEvent;
import com.example.demo.dto.SeriesCreatedEventData;
import com.example.demo.model.Series;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class KafkaPayloadBuilderService {

    public SeriesCreatedEvent seriesCreatedEvent(Series series, String eventType, String sourceService, UUID eventUUID) {

        SeriesCreatedEventData eventData = SeriesCreatedEventData.builder().series_uuid(series.getUuid()).title(series.getTitle()).genre(series.getGenre()).cover_artwork_url(series.getCoverArtworkUrl()).illustrator(series.getIllustrator()).publisher(series.getPublisher()).author(series.getAuthor()).first_print_publication_date(series.getFirstPrintPublicationDate().toString()).last_print_publication_date(series.getLastPrintPublicationDate().toString()).build();

        Metadata metadataPayload = Metadata.builder().event_uuid(eventUUID).event_type(eventType).timestamp(LocalDateTime.now().toString()).source_service(sourceService).build();

        return SeriesCreatedEvent.builder().metadata(metadataPayload).data(eventData).build();
    }

}