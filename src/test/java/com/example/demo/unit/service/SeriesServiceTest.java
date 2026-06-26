package com.example.demo.unit.service;

import com.example.demo.dto.*;
import com.example.demo.event.publish.CatalogueEventPublisher;
import com.example.demo.exception.SeriesAlreadyRegisteredException;
import com.example.demo.factory.SeriesEventFactory;
import com.example.demo.mapper.ChapterMapper;
import com.example.demo.mapper.SeriesMapper;
import com.example.demo.model.Chapter;
import com.example.demo.model.Series;
import com.example.demo.repository.ChapterRepository;
import com.example.demo.repository.SeriesRepository;
import com.example.demo.service.SeriesService;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeriesServiceTest {

    @Mock
    private ChapterRepository chapterRepository;

    @Mock
    private SeriesRepository seriesRepository;

    @Mock
    private SeriesEventFactory seriesEventFactory;


    @Mock
    private CatalogueEventPublisher catalogueEventPublisher;

    @Mock
    private SeriesMapper seriesMapper;

    @Mock
    private ChapterMapper chapterMapper;

    @InjectMocks
    private SeriesService seriesService;

    Series series;

    Chapter chapter;

    SeriesCreatedEvent createdEvent;

    CreateSeriesRequest createSeriesRequest;

    @BeforeEach
    void setUp() {
        series = Instancio.of(Series.class).set(field("title"), "jojo").create();
        chapter = Instancio.create(Chapter.class);
        createdEvent = Instancio.create(SeriesCreatedEvent.class);
        createSeriesRequest = Instancio.of(CreateSeriesRequest.class).set(field(CreateSeriesRequest::title), series.getTitle()).set(field(CreateSeriesRequest::genre), series.getGenre()).set(field(CreateSeriesRequest::author), series.getAuthor()).create();
    }

    @Test
    void getSeries() {
        SeriesFilterDTO filter = new SeriesFilterDTO("Naruto", null, null, null, null);
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "publicationDate"));
        SeriesSummaryDTO dto = new SeriesSummaryDTO(UUID.randomUUID(), "Naruto", "Action", "cover.jpg", "dede", "dede", "dede", LocalDate.now(), LocalDate.now());
        when(seriesRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(new PageImpl<>(List.of(series)));
        when(seriesMapper.toSummaryDto(series)).thenReturn(dto);
        Page<SeriesSummaryDTO> result = seriesService.getSeries(filter, pageable);
        assertEquals(1, result.getTotalElements());
        verify(seriesRepository).findAll(any(Specification.class), eq(pageable));
        verify(seriesMapper).toSummaryDto(series);
    }

    @Test
    void shouldThrowWhenSeriesAlreadyExists() {
        when(seriesRepository.existsByTitle(createSeriesRequest.title())).thenReturn(true);
        assertThrows(SeriesAlreadyRegisteredException.class, () -> seriesService.createSeries(createSeriesRequest));
        verify(seriesRepository, never()).save(any());
        verify(catalogueEventPublisher, never()).publishSeriesCreated(any());
    }

    @Test
    void shouldCreateSeries() {
        when(seriesRepository.existsByTitle(createSeriesRequest.title())).thenReturn(false);
        when(seriesMapper.toEntity(createSeriesRequest)).thenReturn(series);
        when(seriesRepository.save(series)).thenReturn(series);
        Series result = seriesService.createSeries(createSeriesRequest);
        assertEquals(series, result);
        verify(seriesRepository).existsByTitle(createSeriesRequest.title());
        verify(seriesRepository).save(series);
        verify(catalogueEventPublisher).publishSeriesCreated(series);
    }

    @Test
    void shouldReturnSeriesChapters() {
        Pageable pageable = PageRequest.of(0, 10);
        ChapterSummaryDTO dto = Instancio.create(ChapterSummaryDTO.class);
        when(chapterRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(new PageImpl<>(List.of(chapter)));
        when(chapterMapper.toSummaryDto(chapter)).thenReturn(dto);
        Page<ChapterSummaryDTO> result = seriesService.getSeriesChapters(series.getUuid(), pageable);
        assertEquals(1, result.getContent().size());
        verify(chapterRepository).findAll(any(Specification.class), eq(pageable));
        verify(chapterMapper).toSummaryDto(chapter);
    }

    @Test
    void createSeries_false_false_case() {
        when(seriesRepository.existsByTitle(series.getTitle())).thenReturn(true);
        SeriesAlreadyRegisteredException exception = assertThrows(SeriesAlreadyRegisteredException.class, () -> {
            seriesService.createSeries(createSeriesRequest);
        });
        verify(seriesEventFactory, Mockito.times(0)).seriesCreatedEvent(any(Series.class), eq("SERIES_CREATED"), eq("library-app-catalogue-v2"), any(UUID.class));
        verify(seriesRepository, Mockito.times(1)).existsByTitle(series.getTitle());
        verify(seriesRepository, Mockito.times(0)).save(any(Series.class));
    }
}