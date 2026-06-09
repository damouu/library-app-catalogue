package com.example.demo.service;

import com.example.demo.dto.ChapterCreatedEvent;
import com.example.demo.dto.SeriesCreatedEvent;
import com.example.demo.mapper.ChapterMapper;
import com.example.demo.mapper.SeriesMapper;
import com.example.demo.model.Chapter;
import com.example.demo.model.Series;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class KafkaPayloadBuilderServiceTest {

    private KafkaPayloadBuilderService kafkaPayloadBuilderService;


    @BeforeEach
    void setup() {
        kafkaPayloadBuilderService = new KafkaPayloadBuilderService(new SeriesMapper(), new ChapterMapper());
    }

    @Test
    void shouldBuildSeriesCreatedEventSuccessfully() {

        UUID seriesUUID = UUID.randomUUID();
        UUID eventUUID = UUID.randomUUID();
        Series series = Series.builder().uuid(seriesUUID).title("JoJo's Bizarre Adventure").genre("Adventure").author("Hirohiko Araki").illustrator("Hirohiko Araki").publisher("Shueisha").coverArtworkUrl("https://example.com/jojo-cover.jpg").firstPrintPublicationDate(LocalDate.parse("1987-01-01")).lastPrintPublicationDate(LocalDate.parse("2004-04-19")).build();
        SeriesCreatedEvent result = kafkaPayloadBuilderService.seriesCreatedEvent(series, "SERIES_CREATED", "library-app-catalogue-v1", eventUUID);

        assertNotNull(result);

        assertNotNull(result.getMetadata());
        assertEquals(eventUUID, result.getMetadata().getEvent_uuid());
        assertEquals("SERIES_CREATED", result.getMetadata().getEvent_type());
        assertEquals("library-app-catalogue-v1", result.getMetadata().getSource_service());
        assertNotNull(result.getMetadata().getTimestamp());

        assertNotNull(result.getData());
        assertEquals(seriesUUID, result.getData().getSeries_uuid());
        assertEquals("JoJo's Bizarre Adventure", result.getData().getTitle());
        assertEquals("Adventure", result.getData().getGenre());
        assertEquals("https://example.com/jojo-cover.jpg", result.getData().getCover_artwork_url());
        assertEquals("Hirohiko Araki", result.getData().getIllustrator());
        assertEquals("Shueisha", result.getData().getPublisher());
        assertEquals("Hirohiko Araki", result.getData().getAuthor());
        assertEquals("1987-01-01", result.getData().getFirst_print_publication_date());
        assertEquals("2004-04-19", result.getData().getLast_print_publication_date());
    }

    @Test
    void shouldBuildChapterCreatedEventSuccessfully() {

        Series series = Instancio.create(Series.class);
        UUID eventUUID = UUID.randomUUID();
        Chapter chapter = Chapter.builder().uuid(UUID.randomUUID()).title("naruto").secondTitle("naruto").chapterNumber(1).totalPages(110).coverArtworkUrl("https://example.com/naruto-cover.jpg").summary("summary").publicationDate(LocalDate.now()).series(series).build();

        ChapterCreatedEvent result = kafkaPayloadBuilderService.chapterCreatedEvent(chapter, "CHAPTER_CREATED", "library-app-catalogue-v1", eventUUID, 5);

        assertNotNull(result);

        assertNotNull(result.getMetadata());
        assertEquals(eventUUID, result.getMetadata().getEvent_uuid());
        assertEquals("CHAPTER_CREATED", result.getMetadata().getEvent_type());
        assertEquals("library-app-catalogue-v1", result.getMetadata().getSource_service());
        assertNotNull(result.getMetadata().getTimestamp());

        assertNotNull(result.getData());
//        assertEquals(seriesUUID, result.getData().getSeries_uuid());
        assertEquals("naruto", result.getData().getTitle());
        assertEquals("summary", result.getData().getSummary());
        assertEquals("https://example.com/naruto-cover.jpg", result.getData().getCover_artwork_url());
    }
}