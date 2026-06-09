package com.example.demo.unit.service;

import com.example.demo.dto.ChapterCreatedEvent;
import com.example.demo.dto.CreateChapterRequest;
import com.example.demo.mapper.ChapterMapper;
import com.example.demo.model.Chapter;
import com.example.demo.model.Series;
import com.example.demo.repository.ChapterRepository;
import com.example.demo.repository.SeriesRepository;
import com.example.demo.service.ChapterService;
import com.example.demo.service.KafkaPayloadBuilderService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.server.ResponseStatusException;

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
    private KafkaPayloadBuilderService payloadBuilderService;

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
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            chapterService.getChapterUUID(chapter.getUuid());
        }, "Chapter not found");
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("404 NOT_FOUND \"Chapter not found\"", exception.getMessage());
        Mockito.verify(chapterRepository, Mockito.times(1)).findByUuidAndDeletedAtIsNull(chapter.getUuid());
    }

    @Test
    void createChapter_success() {

        CreateChapterRequest request = Instancio.create(CreateChapterRequest.class);

        Series series = Instancio.create(Series.class);

        Chapter chapter = Instancio.create(Chapter.class);

        ChapterCreatedEvent event = Instancio.create(ChapterCreatedEvent.class);

        UUID chapterUuid = UUID.randomUUID();

        event.getData().setChapter_uuid(chapterUuid);

        when(seriesRepository.findByUuid(request.getSeries_uuid())).thenReturn(Optional.of(series));

        when(chapterRepository.existsBySeries_UuidAndChapterNumber(request.getSeries_uuid(), request.getChapter_number())).thenReturn(false);

        when(chapterMapper.toEntity(request, series)).thenReturn(chapter);

        when(payloadBuilderService.chapterCreatedEvent(eq(chapter), eq("CHAPTER_CREATED"), eq("library-app-catalogue-v1"), any(UUID.class), eq(request.getInitial_copies_count()))).thenReturn(event);

        ResponseEntity<String> response = chapterService.createChapter(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        assertEquals(chapterUuid.toString(), response.getBody());

        verify(seriesRepository).findByUuid(request.getSeries_uuid());

        verify(chapterRepository).existsBySeries_UuidAndChapterNumber(request.getSeries_uuid(), request.getChapter_number());

        verify(chapterMapper).toEntity(request, series);

        verify(chapterRepository).save(chapter);

        verify(payloadBuilderService).chapterCreatedEvent(eq(chapter), eq("CHAPTER_CREATED"), eq("library-app-catalogue-v1"), any(UUID.class), eq(request.getInitial_copies_count()));

        verify(kafkaTemplate).send(eq("library.catalog.v1"), any(UUID.class), eq(event));
    }

    @Test
    void createChapter_seriesNotFound() {

        CreateChapterRequest request = Instancio.create(CreateChapterRequest.class);

        when(seriesRepository.findByUuid(request.getSeries_uuid())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> chapterService.createChapter(request));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());

        verify(chapterRepository, never()).save(any());
    }

    @Test
    void createChapter_alreadyExists() {

        CreateChapterRequest request = Instancio.create(CreateChapterRequest.class);

        Series series = Instancio.create(Series.class);

        when(seriesRepository.findByUuid(request.getSeries_uuid())).thenReturn(Optional.of(series));

        when(chapterRepository.existsBySeries_UuidAndChapterNumber(request.getSeries_uuid(), request.getChapter_number())).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> chapterService.createChapter(request));

        assertEquals(HttpStatus.CONFLICT, exception.getStatus());

        verify(chapterRepository, never()).save(any());

        verify(kafkaTemplate, never()).send(anyString(), any(), any());
    }

}