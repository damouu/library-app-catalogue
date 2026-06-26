package com.example.demo.mapper;

import com.example.demo.dto.CreateSeriesRequest;
import com.example.demo.dto.SeriesCreatedEventData;
import com.example.demo.dto.SeriesSummaryDTO;
import com.example.demo.model.Series;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SeriesMapper {

    public SeriesCreatedEventData toEventData(Series series) {
        return new SeriesCreatedEventData(
                series.getUuid(),
                series.getTitle(),
                series.getGenre(),
                series.getAuthor(),
                series.getIllustrator(),
                series.getPublisher(),
                series.getCoverArtworkUrl(),
                series.getFirstPrintPublicationDate().toString(),
                series.getLastPrintPublicationDate().toString());
    }

    public Series toEntity(CreateSeriesRequest request) {
        return Series.builder()
                .uuid(UUID.randomUUID())
                .title(request.title())
                .genre(request.genre())
                .coverArtworkUrl(request.coverArtworkUrl())
                .illustrator(request.illustrator())
                .publisher(request.publisher())
                .author(request.author())
                .firstPrintPublicationDate(request.firstPrintPublicationDate())
                .lastPrintPublicationDate(request.lastPrintPublicationDate())
                .build();
    }

    public SeriesSummaryDTO toSummaryDto(Series series) {
        return new SeriesSummaryDTO(
                series.getUuid(),
                series.getTitle(),
                series.getGenre(),
                series.getCoverArtworkUrl(),
                series.getAuthor(),
                series.getIllustrator(),
                series.getPublisher(),
                series.getFirstPrintPublicationDate(),
                series.getLastPrintPublicationDate());
    }
}
