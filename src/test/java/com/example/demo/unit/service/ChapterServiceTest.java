package com.example.demo.unit.service;

import com.example.demo.dto.CreateChapterRequest;
import com.example.demo.exception.ChapterAlreadyRegisteredException;
import com.example.demo.exception.ChapterNotFoundException;
import com.example.demo.exception.SeriesNotFoundException;
import com.example.demo.mapper.ChapterMapper;
import com.example.demo.model.Chapter;
import com.example.demo.model.Series;
import com.example.demo.repository.ChapterRepository;
import com.example.demo.repository.SeriesRepository;
import com.example.demo.service.ChapterEventPublisher;
import com.example.demo.service.ChapterService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChapterServiceTest {

    @Mock
    private ChapterRepository chapterRepository;

    @Mock
    private SeriesRepository seriesRepository;

    @InjectMocks
    private ChapterService chapterService;

    @Mock
    private ChapterMapper chapterMapper;

    @Mock
    private KafkaTemplate<UUID, Object> kafkaTemplate;

    @Mock
    private ChapterEventPublisher chapterEventPublisher;

    Chapter chapter;

    @BeforeEach
    void setUp() {
        chapter = Instancio.create(Chapter.class);
    }


    @Test
    void getChapters() {
        HashMap<String, String> allParams = new HashMap<String, String>();
        allParams.put("", "");
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "publicationDate"));
        Specification<Chapter> spec = (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%dede%");
        when(chapterRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(new PageImpl<>(List.of(chapter)));
        ResponseEntity<?> responseEntity = chapterService.getChapters(allParams);
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        Mockito.verify(chapterRepository, Mockito.times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void getChapters_Case_Recent() {
        HashMap<String, String> allParams = new HashMap<String, String>();
        allParams.put("type", "recent");
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "publicationDate"));
        Specification<Chapter> spec = (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%dede%");
        when(chapterRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(new PageImpl<>(List.of(chapter)));
        ResponseEntity<?> responseEntity = chapterService.getChapters(allParams);
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        Mockito.verify(chapterRepository, Mockito.times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void getChapters_throw_default() {
        HashMap<String, String> allParams = new HashMap<String, String>();
        allParams.put("type", "popular");
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "publicationDate"));
        Specification<Chapter> spec = (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%dede%");
        lenient().when(chapterRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(chapter)));
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            chapterService.getChapters(allParams);
        }, "Unexpected value:" + allParams.get("type"));
        assertEquals(IllegalStateException.class, exception.getClass());
        assertEquals("Unexpected value: popular", exception.getMessage());
        verifyNoInteractions(chapterRepository);
    }


    @Test
    void getChapterUUID() {
        when(chapterRepository.findByUuidAndDeletedAtIsNull(chapter.getUuid())).thenReturn(Optional.ofNullable(chapter));
        Chapter responseEntity = chapterService.getChapterUUID(chapter.getUuid());
        assertEquals(responseEntity.getUuid(), chapter.getUuid());
        Mockito.verify(chapterRepository, Mockito.times(1)).findByUuidAndDeletedAtIsNull(chapter.getUuid());
    }


    @Test
    void getChapterUUID_Throw_Exception() {
        ChapterNotFoundException exception = assertThrows(ChapterNotFoundException.class, () -> {
            chapterService.getChapterUUID(chapter.getUuid());
        }, "Chapter not found");
        assertEquals("Could not find chapter with uuid " + chapter.getUuid(), exception.getMessage());
        Mockito.verify(chapterRepository, Mockito.times(1)).findByUuidAndDeletedAtIsNull(chapter.getUuid());
    }

    @Test
    void createChapter_success() {

        CreateChapterRequest request = Instancio.create(CreateChapterRequest.class);
        Series series = Instancio.create(Series.class);
        Chapter savedChapter = Instancio.create(Chapter.class);
        when(chapterMapper.toEntity(request, series)).thenReturn(chapter);
        when(seriesRepository.findByUuid(request.getSeries_uuid())).thenReturn(Optional.of(series));
        when(chapterRepository.existsBySeries_UuidAndChapterNumber(request.getSeries_uuid(), request.getChapter_number())).thenReturn(false);
        when(chapterRepository.save(any(Chapter.class))).thenReturn(savedChapter);
        Chapter result = chapterService.createChapter(request);
        assertEquals(savedChapter, result);
        verify(chapterRepository).save(any(Chapter.class));
        verify(chapterEventPublisher).publishChapterCreated(savedChapter, request.getInitial_copies_count());
    }

    @Test
    void createChapter_seriesNotFound() {
        CreateChapterRequest request = Instancio.create(CreateChapterRequest.class);
        when(seriesRepository.findByUuid(request.getSeries_uuid())).thenReturn(Optional.empty());
        SeriesNotFoundException exception = assertThrows(SeriesNotFoundException.class, () -> chapterService.createChapter(request));
        assertEquals("Could not find series with UUID" + request.getSeries_uuid(), exception.getMessage());
        verify(chapterRepository, never()).save(any());
    }

    @Test
    void createChapter_alreadyExists() {
        CreateChapterRequest request = Instancio.create(CreateChapterRequest.class);
        Series series = Instancio.create(Series.class);
        when(seriesRepository.findByUuid(request.getSeries_uuid())).thenReturn(Optional.of(series));
        when(chapterRepository.existsBySeries_UuidAndChapterNumber(request.getSeries_uuid(), request.getChapter_number())).thenReturn(true);
        ChapterAlreadyRegisteredException exception = assertThrows(ChapterAlreadyRegisteredException.class, () -> chapterService.createChapter(request));
        assertEquals("この巻は既に登録されています " + request.getChapter_number(), exception.getMessage());
        verify(chapterRepository, never()).save(any());
        verify(kafkaTemplate, never()).send(anyString(), any(), any());
    }

}