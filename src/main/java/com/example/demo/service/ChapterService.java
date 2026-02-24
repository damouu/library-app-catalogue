package com.example.demo.service;

import com.example.demo.model.Chapter;
import com.example.demo.repository.ChapterRepository;
import com.example.demo.spec.ChapterSpecification;
import com.example.demo.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    @Transactional()
    public Chapter getChapterUUID(UUID chapterUUID) {
        Chapter chapter = chapterRepository.findByUuidAndDeletedAtIsNull(chapterUUID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chapter not found"));
        chapter.setSeriesUuid(chapter.getSeries().getUuid());
        return ResponseEntity.status(HttpStatus.OK).body(chapter).getBody();
    }

}
