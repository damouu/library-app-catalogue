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
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.instancio.Select.field;

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
    void getChapterTOP2_Not_Throw_Exception() {
        List<UUID> uuids = Stream.generate(UUID::randomUUID).limit(1).collect(Collectors.toList());
        List<Chapter> chapters = Stream.generate(() -> Instancio.of(Chapter.class).set(field(Chapter::getChapterUUID), uuids.get(0)).create()).limit(1).toList();
        Mockito.when(chapterRepository.findByChapterUUIDInAndDeletedATIsNull(uuids)).thenReturn(chapters);
        HashMap<String, List<Chapter>> responseEntity = chapterService.getChapters(uuids);
        Mockito.verify(chapterRepository, Mockito.times(1)).findByChapterUUIDInAndDeletedATIsNull(uuids);
        Assertions.assertFalse(responseEntity.isEmpty());
        Assertions.assertTrue(responseEntity.containsKey("chapters"));
        Assertions.assertTrue(responseEntity.containsValue(chapters));
        Assertions.assertSame(Chapter.class, responseEntity.get("chapters").getFirst().getClass());
        Assertions.assertSame(responseEntity.get("chapters").getFirst().getChapterUUID(), uuids.getFirst());
    }

    @Test
    void getChapterTOP2_Throw_Exception() {
        List<UUID> uuids = Stream.generate(UUID::randomUUID).limit(1).collect(Collectors.toList());
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            chapterService.getChapters(uuids);
        }, "One or more chapters do not exist or are deleted. Missing UUIDs");
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        Assertions.assertEquals("404 NOT_FOUND \"One or more chapters do not exist or are deleted. Missing UUIDs\"", exception.getMessage());
    }


}