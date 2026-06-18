package com.example.demo.service;

import com.example.demo.dto.CreateChapterRequest;
import com.example.demo.exception.ChapterAlreadyRegisteredException;
import com.example.demo.exception.ChapterNotFoundException;
import com.example.demo.exception.SeriesNotFoundException;
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
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * The type Chapter service.
 */
@Service
@RequiredArgsConstructor
public class ChapterService {

    private final ChapterRepository chapterRepository;

    private final SeriesRepository seriesRepository;

    private final ChapterMapper chapterMapper;

    private final CatalogueEventPublisher catalogueEventPublisher;


    /**
     * Gets chapters.
     *
     * @param allParams the all params
     * @return chapters
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
     * 登録されている巻を検索する。無い場合例外を発生されます。
     *
     * @param chapterUUID the chapter uuid
     * @return the chapter uuid
     * @throws ChapterNotFoundException ChapterNotFoundException
     */
    public Chapter getChapterUUID(UUID chapterUUID) {
        return chapterRepository.findByUuidAndDeletedAtIsNull(chapterUUID).orElseThrow(() -> new ChapterNotFoundException(chapterUUID));
    }

    /**
     * 新しい巻登録されたらカフカイベントを発行、配信されます。
     *
     * @param chapterRequest the chapter request
     * @return Chapter
     * @throws ChapterAlreadyRegisteredException chapterAlreadyRegisteredException
     * @throws SeriesNotFoundException           seriesNotFoundException
     */
    @Transactional
    public Chapter createChapter(CreateChapterRequest chapterRequest) {
        Series series = (Series) seriesRepository.findByUuid((chapterRequest.getSeries_uuid())).orElseThrow(() -> new SeriesNotFoundException(chapterRequest.getSeries_uuid()));
        if (chapterRepository.existsBySeries_UuidAndChapterNumber(chapterRequest.getSeries_uuid(), chapterRequest.getChapter_number())) {
            throw new ChapterAlreadyRegisteredException(chapterRequest.getChapter_number());
        }
        Chapter chapter = chapterMapper.toEntity(chapterRequest, series);
        Chapter savedChapter = chapterRepository.save(chapter);
        catalogueEventPublisher.publishChapterCreated(savedChapter, chapterRequest.getInitial_copies_count());
        return savedChapter;
    }

}
