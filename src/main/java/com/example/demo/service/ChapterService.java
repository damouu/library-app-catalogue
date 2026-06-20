package com.example.demo.service;

import com.example.demo.dto.ChapterFilterDTO;
import com.example.demo.dto.ChapterSummaryDTO;
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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
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
     * @param filter   dede
     * @param pageable dede
     * @return ChapterSummaryDTO
     */
    public Page<ChapterSummaryDTO> getChapters(ChapterFilterDTO filter, Pageable pageable) {
        LocalDate startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = LocalDate.now().with(DayOfWeek.SUNDAY);
        Specification<Chapter> specification = "recent".equals(filter.type()) ? ChapterSpecification.publishedBetween(startOfWeek, endOfWeek) : ChapterSpecification.filterChapter(filter);
        return chapterRepository.findAll(specification, pageable).map(chapterMapper::toSummaryDto);
    }


    /**
     * s
     * 登録されている巻を検索する。無い場合例外を発生されます。
     *
     * @param chapterUUID the chapter uuid
     * @return the chapter uuid
     * @throws ChapterNotFoundException ChapterNotFoundException
     */
    public ChapterSummaryDTO getChapterUUID(UUID chapterUUID) {
        Chapter chapter = chapterRepository.findByUuidAndDeletedAtIsNull(chapterUUID).orElseThrow(() -> new ChapterNotFoundException(chapterUUID));
        return chapterMapper.toSummaryDto(chapter);
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
        Series series = (Series) seriesRepository.findByUuid((chapterRequest.series_uuid())).orElseThrow(() -> new SeriesNotFoundException(chapterRequest.series_uuid()));
        if (chapterRepository.existsBySeries_UuidAndChapterNumber(chapterRequest.series_uuid(), chapterRequest.chapter_number())) {
            throw new ChapterAlreadyRegisteredException(chapterRequest.chapter_number());
        }
        Chapter chapter = chapterMapper.toEntity(chapterRequest, series);
        Chapter savedChapter = chapterRepository.save(chapter);
        catalogueEventPublisher.publishChapterCreated(savedChapter, chapterRequest.initial_copies_count());
        return savedChapter;
    }

}
