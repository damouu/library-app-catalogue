package com.example.demo.controller;

import com.example.demo.dto.ChapterFilterDTO;
import com.example.demo.dto.ChapterSummaryDTO;
import com.example.demo.dto.CreateChapterRequest;
import com.example.demo.model.Chapter;
import com.example.demo.service.ChapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/public/chapters")
public class ChapterController {

    private final ChapterService chapterService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<ChapterSummaryDTO> getChapter(ChapterFilterDTO filter, Pageable pageable) {
        return chapterService.getChapters(filter, pageable);
    }

    @GetMapping(path = "/{chapterUUID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ChapterSummaryDTO getChapterUUID(@PathVariable UUID chapterUUID) {
        return chapterService.getChapterUUID(chapterUUID);
    }

    @PostMapping
    public Chapter createSeries(@RequestBody CreateChapterRequest chapterRequest) {
        return chapterService.createChapter(chapterRequest);
    }

}
