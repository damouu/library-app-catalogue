package com.example.demo.service;

import com.example.demo.dto.ChapterSummaryDTO;
import com.example.demo.dto.CreateSeriesRequest;
import com.example.demo.exception.SeriesAlreadyRegisteredException;
import com.example.demo.mapper.ChapterMapper;
import com.example.demo.mapper.SeriesMapper;
import com.example.demo.model.Chapter;
import com.example.demo.model.Series;
import com.example.demo.repository.ChapterRepository;
import com.example.demo.repository.SeriesRepository;
import com.example.demo.spec.ChapterSpecification;
import com.example.demo.spec.SeriesSpecification;
import com.example.demo.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.UUID;

/**
 * The type Series service.
 */
@Service
@RequiredArgsConstructor
public class SeriesService {

    private final SeriesRepository seriesRepository;

    private final ChapterRepository chapterRepository;

    private final SeriesMapper seriesMapper;

    private final ChapterMapper chapterMapper;

    private final CatalogueEventPublisher catalogueEventPublisher;


    /**
     * Gets series.
     *
     * @param allParams the all params
     * @return series
     */
    public ResponseEntity<Page<Series>> getSeries(Map allParams) {
        Pageable pageRequest = PaginationUtil.extractPage(allParams);
        Specification<Series> specification = SeriesSpecification.filterSeries(allParams);
        final Page<Series> series = seriesRepository.findAll(specification, pageRequest);
        return ResponseEntity.status(HttpStatus.OK).body(series);
    }

    /**
     * Gets series chapters.
     *
     * @param allParams  the all params
     * @param seriesUUID the series uuid
     * @return series chapters
     */
    public Page<ChapterSummaryDTO> getSeriesChapters(UUID seriesUUID, Pageable pageable) {
        Specification<Chapter> specification = ChapterSpecification.belongsToSeries(seriesUUID).and(ChapterSpecification.notDeleted());
        return chapterRepository.findAll(specification, pageable).map(chapterMapper::toSummaryDto);
    }


    /**
     * Create series series.
     *
     * @param seriesRequest CreateSeriesRequest
     * @return the series
     */
    @Transactional
    public Series createSeries(CreateSeriesRequest seriesRequest) {
        if (seriesRepository.existsByTitle(seriesRequest.getTitle())) {
            throw new SeriesAlreadyRegisteredException(seriesRequest);
        }
        Series series = seriesMapper.toEntity(seriesRequest);
        Series seriesSaved = seriesRepository.save(series);
        catalogueEventPublisher.publishSeriesCreated(seriesSaved);
        return seriesSaved;
    }
}
