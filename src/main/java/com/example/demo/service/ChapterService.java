package com.example.demo.service;

import com.example.demo.dto.ChapterCreatedEvent;
import com.example.demo.dto.CreateChapterRequest;
import com.example.demo.mapper.ChapterMapper;
import com.example.demo.model.Chapter;
import com.example.demo.model.Series;
import com.example.demo.repository.ChapterRepository;
import com.example.demo.repository.SeriesRepository;
import com.example.demo.spec.ChapterSpecification;
import com.example.demo.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChapterService {

    private final ChapterRepository chapterRepository;

    private final SeriesRepository seriesRepository;

    private final ChapterMapper chapterMapper;

    private final KafkaTemplate<UUID, Object> KafkaTemplate;

    private final KafkaPayloadBuilderService payloadBuilderService;


    /**
     * @param allParams
     * @return
     */
    public ResponseEntity<?> getChapters(Map allParams) {
        LocalDate startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = LocalDate.now().with(DayOfWeek.SUNDAY);
        Pageable pageable = PaginationUtil.extractPage(allParams);
        String typeValue = Optional.ofNullable(allParams.get("type")).map(Object::toString).orElse(null);
        Page<Chapter> chapters;
        switch (typeValue) {
            case ("recent"):
                Specification<Chapter> specification = ChapterSpecification.publishedBetween(startOfWeek, endOfWeek);
                chapters = chapterRepository.findAll(specification, pageable);
                return ResponseEntity.status(HttpStatus.OK).body(chapters);
            case null:
                specification = ChapterSpecification.filterChapter(allParams);
                chapters = chapterRepository.findAll(specification, pageable);
                return ResponseEntity.status(HttpStatus.OK).body(chapters);
            default:
                throw new IllegalStateException("Unexpected value: " + typeValue);
        }
    }

    /**
     * @param chapterUUID
     * @return
     */
    public Chapter getChapterUUID(UUID chapterUUID) {
        Chapter chapter = chapterRepository.findByUuidAndDeletedAtIsNull(chapterUUID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chapter not found"));
        return ResponseEntity.status(HttpStatus.OK).body(chapter).getBody();
    }

    /**
     * @param chapterRequest
     * @return
     */
    @Transactional
    public ResponseEntity<String> createChapter(CreateChapterRequest chapterRequest) {
        Series series = (Series) seriesRepository.findByUuid((chapterRequest.getSeries_uuid())).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "シリーズが見つかりません。"));
        if (chapterRepository.existsBySeries_UuidAndChapterNumber(chapterRequest.getSeries_uuid(), chapterRequest.getChapter_number())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "この巻は既に登録されています。");
        }
        Chapter chapter = chapterMapper.toEntity(chapterRequest, series);
        UUID eventUUID = UUID.randomUUID();
        ChapterCreatedEvent chapterCreatedEvent = payloadBuilderService.chapterCreatedEvent(chapter, "CHAPTER_CREATED", "library-app-catalogue-v1", eventUUID, chapterRequest.getInitial_copies_count());
        chapterRepository.save(chapter);
        KafkaTemplate.send("library.catalog.v1", eventUUID, chapterCreatedEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(chapterCreatedEvent.getData().getChapter_uuid().toString());
    }

}
