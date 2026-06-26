package com.example.demo.controller;

import com.example.demo.dto.ChapterSummaryDTO;
import com.example.demo.dto.CreateSeriesRequest;
import com.example.demo.dto.SeriesFilterDTO;
import com.example.demo.dto.SeriesSummaryDTO;
import com.example.demo.model.Series;
import com.example.demo.search.SearchWhitelistSeries;
import com.example.demo.service.SeriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/public/series")
public class SeriesController {

    private final SeriesService seriesService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<SeriesSummaryDTO> getSeries(SeriesFilterDTO filter, Pageable pageable, @RequestParam MultiValueMap<String, String> params) {
        SearchWhitelistSeries.validate(params.keySet());
        SearchWhitelistSeries.validateSort(pageable.getSort(), SearchWhitelistSeries.SERIES_SORT_FIELDS);
        return seriesService.getSeries(filter, pageable);
    }

    @GetMapping(path = "/{seriesUUID}/chapters", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<ChapterSummaryDTO> getSeriesChapters(Pageable pageable, @PathVariable UUID seriesUUID) {
        return seriesService.getSeriesChapters(seriesUUID, pageable);
    }

    @PostMapping
    public Series createSeries(@RequestBody CreateSeriesRequest seriesRequest) {
        return seriesService.createSeries(seriesRequest);
    }

}
