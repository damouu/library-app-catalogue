package com.example.demo.service;

import com.example.demo.dto.ChapterCreatedEvent;
import com.example.demo.dto.SeriesCreatedEvent;
import com.example.demo.factory.ChapterEventFactory;
import com.example.demo.factory.SeriesEventFactory;
import com.example.demo.mapper.ChapterMapper;
import com.example.demo.mapper.SeriesMapper;
import com.example.demo.model.Chapter;
import com.example.demo.model.Series;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class KafkaPayloadBuilderServiceTest {


    SeriesEventFactory seriesEventFactory = new SeriesEventFactory(new SeriesMapper());

    ChapterEventFactory chapterEventFactory = new ChapterEventFactory(new ChapterMapper());


    @Test
    void shouldBuildSeriesCreatedEventSuccessfully() {
        UUID seriesUUID = UUID.randomUUID();
        UUID eventUUID = UUID.randomUUID();
        Series series = Series.builder().uuid(seriesUUID).title("JoJo's Bizarre Adventure").genre("Adventure").author("Hirohiko Araki").illustrator("Hirohiko Araki").publisher("Shueisha").coverArtworkUrl("https://example.com/jojo-cover.jpg").firstPrintPublicationDate(LocalDate.parse("1987-01-01")).lastPrintPublicationDate(LocalDate.parse("2004-04-19")).build();
        SeriesCreatedEvent result = seriesEventFactory.seriesCreatedEvent(series, "SERIES_CREATED", "library-app-catalogue-v2", eventUUID);
        assertNotNull(result);
        assertNotNull(result.metadata());
        assertEquals(eventUUID, result.metadata().event_uuid());
        assertEquals("SERIES_CREATED", result.metadata().event_type());
        assertEquals("library-app-catalogue-v2", result.metadata().source_service());
        assertNotNull(result.metadata().timestamp());
        assertNotNull(result.data());
        assertEquals(seriesUUID, result.data().series_uuid());
        assertEquals("JoJo's Bizarre Adventure", result.data().title());
        assertEquals("Adventure", result.data().genre());
        assertEquals("https://example.com/jojo-cover.jpg", result.data().cover_artwork_url());
        assertEquals("Hirohiko Araki", result.data().illustrator());
        assertEquals("Shueisha", result.data().publisher());
        assertEquals("Hirohiko Araki", result.data().author());
        assertEquals("1987-01-01", result.data().first_print_publication_date());
        assertEquals("2004-04-19", result.data().last_print_publication_date());
    }

    @Test
    void shouldBuildChapterCreatedEventSuccessfully() {
        Series series = Instancio.create(Series.class);
        UUID eventUUID = UUID.randomUUID();
        Chapter chapter = Chapter.builder().uuid(UUID.randomUUID()).title("naruto").secondTitle("naruto").chapterNumber(1).totalPages(110).coverArtworkUrl("https://example.com/naruto-cover.jpg").summary("summary").publicationDate(LocalDate.now()).series(series).build();
        ChapterCreatedEvent result = chapterEventFactory.chapterCreatedEvent(chapter, "CHAPTER_CREATED", "library-app-catalogue-v2", eventUUID, 5);
        assertNotNull(result);
        assertNotNull(result.metadata());
        assertEquals(eventUUID, result.metadata().event_uuid());
        assertEquals("CHAPTER_CREATED", result.metadata().event_type());
        assertEquals("library-app-catalogue-v2", result.metadata().source_service());
        assertNotNull(result.metadata().timestamp());
        assertNotNull(result.data());
        assertEquals(series.getUuid(), result.data().series_uuid());
        assertEquals("naruto", result.data().title());
        assertEquals("summary", result.data().summary());
        assertEquals("https://example.com/naruto-cover.jpg", result.data().cover_artwork_url());
    }
}