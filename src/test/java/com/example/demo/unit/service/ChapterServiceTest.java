package com.example.demo.unit.service;

import com.example.demo.model.Chapter;
import com.example.demo.repository.ChapterRepository;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChapterServiceTest {

    @Mock
    private ChapterRepository chapterRepository;

    @InjectMocks
    private ChapterService chapterService;

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
        IllegalStateException exception = Assertions.assertThrows(IllegalStateException.class, () -> {
            chapterService.getChapters(allParams);
        }, "Unexpected value:" + allParams.get("type"));
        Assertions.assertEquals(IllegalStateException.class, exception.getClass());
        Assertions.assertEquals("Unexpected value: popular", exception.getMessage());
        verifyNoInteractions(chapterRepository);
    }


    @Test
    void getChapterUUID() {
        when(chapterRepository.findByUuidAndDeletedAtIsNull(chapter.getUuid())).thenReturn(Optional.ofNullable(chapter));
        Chapter responseEntity = chapterService.getChapterUUID(chapter.getUuid());
        Assertions.assertEquals(responseEntity.getUuid(), chapter.getUuid());
        Mockito.verify(chapterRepository, Mockito.times(1)).findByUuidAndDeletedAtIsNull(chapter.getUuid());
    }


    @Test
    void getChapterUUID_Throw_Exception() {
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            chapterService.getChapterUUID(chapter.getUuid());
        }, "chapter does not exist");
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        Assertions.assertEquals("404 NOT_FOUND \"chapter does not exist\"", exception.getMessage());
        Mockito.verify(chapterRepository, Mockito.times(1)).findByUuidAndDeletedAtIsNull(chapter.getUuid());
    }

}