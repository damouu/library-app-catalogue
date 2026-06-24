package com.example.demo.service;

import com.example.demo.dto.ChapterSummaryDTO;
import com.example.demo.dto.CreateSeriesRequest;
import com.example.demo.dto.SeriesFilterDTO;
import com.example.demo.dto.SeriesSummaryDTO;
import com.example.demo.event.publish.CatalogueEventPort;
import com.example.demo.exception.SeriesAlreadyRegisteredException;
import com.example.demo.mapper.ChapterMapper;
import com.example.demo.mapper.SeriesMapper;
import com.example.demo.model.Chapter;
import com.example.demo.model.Series;
import com.example.demo.repository.ChapterRepository;
import com.example.demo.repository.SeriesRepository;
import com.example.demo.spec.ChapterSpecification;
import com.example.demo.spec.SeriesSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    private final CatalogueEventPort catalogueEventPort;


    /**
     * Gets series.
     *
     * @param filter filter
     * @param pageable pageable
     * @return SeriesSummaryDTO
     */
    public Page<SeriesSummaryDTO> getSeries(SeriesFilterDTO filter, Pageable pageable) {
        Specification<Series> specification = SeriesSpecification.filterSeries(filter);
        return seriesRepository.findAll(specification, pageable).map(seriesMapper::toSummaryDto);
    }

    /**
     * Gets series chapters.
     *
     * @param pageable pageable
     * @param seriesUUID seriesUUID
     * @return series chapters
     */
    public Page<ChapterSummaryDTO> getSeriesChapters(UUID seriesUUID, Pageable pageable) {
        Specification<Chapter> specification = ChapterSpecification.belongsToSeries(seriesUUID).and(ChapterSpecification.notDeleted());
        return chapterRepository.findAll(specification, pageable).map(chapterMapper::toSummaryDto);
    }


    /**
     * Create series series.
     *
     * @param seriesRequest seriesRequest
     * @return a series
     * @throws SeriesAlreadyRegisteredException SeriesAlreadyRegisteredException
     */
    @Transactional
    public Series createSeries(CreateSeriesRequest seriesRequest) {
        if (seriesRepository.existsByTitle(seriesRequest.title())) {
            throw new SeriesAlreadyRegisteredException(seriesRequest.title());
        }
        Series series = seriesMapper.toEntity(seriesRequest);
        Series seriesSaved = seriesRepository.save(series);
        catalogueEventPort.publishSeriesCreated(seriesSaved);
        return seriesSaved;
    }
}
