package com.example.demo.service;

import com.example.demo.dto.CreateSeriesRequest;
import com.example.demo.dto.SeriesCreatedEvent;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeriesService {

    private final SeriesRepository seriesRepository;

    private final ChapterRepository chapterRepository;

    private final SeriesMapper seriesMapper;

    private final KafkaTemplate<UUID, Object> KafkaTemplate;

    private final KafkaPayloadBuilderService payloadBuilderService;


    /**
     * @param allParams
     * @return
     */
    public ResponseEntity<Page<Series>> getSeries(Map allParams) {
        Pageable pageRequest = PaginationUtil.extractPage(allParams);
        Specification<Series> specification = SeriesSpecification.filterSeries(allParams);
        final Page<Series> series = seriesRepository.findAll(specification, pageRequest);
        return ResponseEntity.status(HttpStatus.OK).body(series);
    }

    /**
     * @param allParams
     * @param seriesUUID
     * @return
     */
    public ResponseEntity<Page<Chapter>> getSeriesChapters(Map allParams, UUID seriesUUID) {
        PageRequest pageRequest = (PageRequest) PaginationUtil.extractPage(allParams);
        Page<Chapter> chapters;
        boolean hasPageParam = allParams.containsKey("page");
        boolean hasSizeParam = allParams.containsKey("size");
        Optional<Series> series = seriesRepository.findByUuidAndDeletedAtIsNull(seriesUUID);
        if (hasPageParam && hasSizeParam) {
            chapters = chapterRepository.findBySeriesUuid(seriesUUID, pageRequest);
        } else {
            Specification<Chapter> baseSpecification = (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.and(criteriaBuilder.and(criteriaBuilder.equal(root.get("series"), series.get()), criteriaBuilder.isNull(root.get("deletedAT"))));
            Specification<Chapter> chapterSpecification = ChapterSpecification.filterChapter(allParams);
            Specification<Chapter> finalSpec = baseSpecification.and(chapterSpecification);
            chapters = chapterRepository.findAll(finalSpec, pageRequest);
        }
        return ResponseEntity.status(HttpStatus.OK).body(chapters);
    }

    /**
     * @param request
     * @return
     */
    @Transactional
    public ResponseEntity<String> createSeries(CreateSeriesRequest request) {
        if (seriesRepository.existsByTitle(request.getTitle())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "このシリーズは既に登録されています。");
        }
        Series series = seriesMapper.toEntity(request);
        UUID eventUUID = UUID.randomUUID();
        SeriesCreatedEvent createdEvent = payloadBuilderService.seriesCreatedEvent(series, "SERIES_CREATED", "library-app-catalogue-v1", eventUUID);
        seriesRepository.save(series);
        KafkaTemplate.send("library.catalog.v1", eventUUID, createdEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent.getData().getSeries_uuid().toString());
    }
}
