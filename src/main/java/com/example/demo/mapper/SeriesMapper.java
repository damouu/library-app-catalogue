package com.example.demo.mapper;

import com.example.demo.dto.CreateSeriesRequest;
import com.example.demo.dto.SeriesCreatedEventData;
import com.example.demo.model.Series;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SeriesMapper {
    public SeriesCreatedEventData toEventData(Series series) {

        return SeriesCreatedEventData.builder()
                .series_uuid(series.getUuid())
                .title(series.getTitle())
                .genre(series.getGenre())
                .author(series.getAuthor())
                .illustrator(series.getIllustrator())
                .publisher(series.getPublisher())
                .cover_artwork_url(series.getCoverArtworkUrl())
                .first_print_publication_date(series.getFirstPrintPublicationDate().toString())
                .last_print_publication_date(series.getLastPrintPublicationDate().toString())
                .build();
    }

    public Series toEntity(CreateSeriesRequest request) {
        return Series.builder()
                .uuid(UUID.randomUUID())
                .title(request.getTitle())
                .genre(request.getGenre())
                .coverArtworkUrl(request.getCoverArtworkUrl())
                .illustrator(request.getIllustrator())
                .publisher(request.getPublisher())
                .author(request.getAuthor())
                .firstPrintPublicationDate(request.getFirstPrintPublicationDate())
                .lastPrintPublicationDate(request.getLastPrintPublicationDate())
                .build();
    }
}
