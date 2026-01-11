package com.example.demo.unit.service;

import com.example.demo.model.Chapter;
import com.example.demo.model.Series;
import com.example.demo.repository.ChapterRepository;
import com.example.demo.repository.SeriesRepository;
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

import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SeriesServiceTest {

    @Mock
    private ChapterRepository chapterRepository;

    @Mock
    private SeriesRepository seriesRepository;

    @InjectMocks
    private SeriesService seriesService;

    Series series;

    Chapter chapter;

    @BeforeEach
    void setUp() {
        series = Instancio.create(Series.class);
        chapter = Instancio.create(Chapter.class);

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
}