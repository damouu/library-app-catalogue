package com.example.demo.integration.controller;

import com.example.demo.dto.CreateSeriesRequest;
import com.example.demo.exception.ChapterAlreadyRegisteredException;
import com.example.demo.exception.ChapterNotFoundException;
import com.example.demo.exception.SeriesAlreadyRegisteredException;
import com.example.demo.exception.SeriesNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class TestExceptionController {

    @GetMapping("/test/chapter/conflict")
    void chapterConflict() {
        throw new ChapterAlreadyRegisteredException(1);
    }

    @GetMapping("/test/series/conflict")
    void seriesConflict() {
        throw new SeriesAlreadyRegisteredException("naruto");
    }

    @GetMapping("/test/series/notfound")
    void seriesNotFound() {
        throw new SeriesNotFoundException(UUID.randomUUID());
    }

    @GetMapping("/test/chapter/notfound")
    void chapterNotFound() {
        throw new ChapterNotFoundException(UUID.randomUUID());
    }
}