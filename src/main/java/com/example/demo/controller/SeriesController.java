package com.example.demo.controller;

import com.example.demo.model.Chapter;
import com.example.demo.model.Series;
import com.example.demo.service.SeriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/public/series")
public class SeriesController {

    private final SeriesService seriesService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Series>> getSeries(@RequestParam Map<String, ?> allParams) {
        return seriesService.getSeries(allParams);
    }

    @GetMapping(path = "/{seriesUUID}/chapters", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Chapter>> getSeriesChapters(@RequestParam Map<String, ?> allParams, @PathVariable UUID seriesUUID) {
        return seriesService.getSeriesChapters(allParams, seriesUUID);
    }

}
