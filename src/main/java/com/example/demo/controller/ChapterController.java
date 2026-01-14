package com.example.demo.controller;

import com.example.demo.model.Chapter;
import com.example.demo.service.ChapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@Validated
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("api/public/chapters")
public class ChapterController {

    private final ChapterService chapterService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getChapter(@RequestParam Map<String, ?> allParams) {
        return chapterService.getChapters(allParams);
    }

    @GetMapping(path = "/{chapterUUID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Chapter getChapterUUID(@PathVariable UUID chapterUUID) {
        return chapterService.getChapterUUID(chapterUUID);
    }

}
