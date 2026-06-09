package com.example.demo.unit.service;

import com.example.demo.dto.CreateSeriesRequest;
import com.example.demo.dto.SeriesCreatedEvent;
import com.example.demo.mapper.SeriesMapper;
import com.example.demo.model.Chapter;
import com.example.demo.model.Series;
import com.example.demo.repository.ChapterRepository;
import com.example.demo.repository.SeriesRepository;
import com.example.demo.service.KafkaPayloadBuilderService;
import com.example.demo.service.SeriesService;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SeriesServiceTest {

    @Mock
    private ChapterRepository chapterRepository;

    @Mock
    private SeriesRepository seriesRepository;

    @Mock
    private KafkaPayloadBuilderService payloadBuilderService;

    @Mock
    private KafkaTemplate<UUID, Object> kafkaTemplate;

    @Mock
    private SeriesMapper seriesMapper;

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
        createSeriesRequest = Instancio.of(CreateSeriesRequest.class).set(field(CreateSeriesRequest::getTitle), series.getTitle()).set(field(CreateSeriesRequest::getGenre), series.getGenre()).set(field(CreateSeriesRequest::getAuthor), series.getAuthor()).create();
    }

    @Test
    void getSeries() {
        HashMap<String, String> allParams = new HashMap<String, String>();
        allParams.put("", "");
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "publicationDate"));
        Specification<Series> spec = (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%dede%");
        when(seriesRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(new PageImpl<>(List.of(series)));
        ResponseEntity<?> responseEntity = seriesService.getSeries(allParams);
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        verify(seriesRepository, Mockito.times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void getSeriesChapters_false_case() {
        HashMap<String, String> allParams = new HashMap<>();
        when(chapterRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(chapter)));
        ResponseEntity<?> responseEntity = seriesService.getSeriesChapters(allParams, series.getUuid());
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        verify(chapterRepository, Mockito.times(1)).findAll(any(Specification.class), any(Pageable.class));
    }


    @Test
    void getSeriesChapters_true_case() {
        HashMap<String, String> allParams = new HashMap<String, String>();
        allParams.put("page", "0");
        allParams.put("size", "10");
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "publicationDate"));
        when(chapterRepository.findBySeriesUuid(eq(series.getUuid()), eq(pageable))).thenReturn(new PageImpl<>(List.of(chapter)));
        ResponseEntity<?> responseEntity = seriesService.getSeriesChapters(allParams, series.getUuid());
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        verify(chapterRepository, Mockito.times(1)).findBySeriesUuid(series.getUuid(), pageable);
    }

    @Test
    void getSeriesChapters_true_false_case() {
        HashMap<String, String> allParams = new HashMap<String, String>();
        allParams.put("page", "0");
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "publicationDate"));
        when(chapterRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(chapter)));
        ResponseEntity<?> responseEntity = seriesService.getSeriesChapters(allParams, series.getUuid());
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        verify(chapterRepository, Mockito.times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void getSeriesChapters_false_true_case() {
        HashMap<String, String> allParams = new HashMap<String, String>();
        allParams.put("size", "1");
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "publicationDate"));
        when(chapterRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(chapter)));
        ResponseEntity<?> responseEntity = seriesService.getSeriesChapters(allParams, series.getUuid());
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        verify(chapterRepository, Mockito.times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void createSeries_false_true_case() {
        when(seriesRepository.existsByTitle(series.getTitle())).thenReturn(false);
        when(seriesMapper.toEntity(any(CreateSeriesRequest.class))).thenReturn(series);
        when(payloadBuilderService.seriesCreatedEvent(any(Series.class), eq("SERIES_CREATED"), eq("library-app-catalogue-v1"), any(UUID.class))).thenReturn(createdEvent);
        when(kafkaTemplate.send(anyString(), any(UUID.class), any())).thenReturn(null);
        ResponseEntity<?> responseEntity = seriesService.createSeries(createSeriesRequest);
        verify(payloadBuilderService, Mockito.times(1)).seriesCreatedEvent(any(Series.class), eq("SERIES_CREATED"), eq("library-app-catalogue-v1"), any(UUID.class));
        verify(seriesRepository, Mockito.times(1)).existsByTitle(series.getTitle());
        verify(seriesRepository).save(any(Series.class));
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    void createSeries_false_false_case() {
        when(seriesRepository.existsByTitle(series.getTitle())).thenReturn(true);
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            seriesService.createSeries(createSeriesRequest);
        });
        verify(payloadBuilderService, Mockito.times(0)).seriesCreatedEvent(any(Series.class), eq("SERIES_CREATED"), eq("library-app-catalogue-v1"), any(UUID.class));
        verify(seriesRepository, Mockito.times(1)).existsByTitle(series.getTitle());
        verify(seriesRepository, Mockito.times(0)).save(any(Series.class));
        Assertions.assertTrue(exception.getStatus().isError());
    }
}