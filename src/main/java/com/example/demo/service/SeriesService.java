package com.example.demo.service;

import com.example.demo.model.Chapter;
import com.example.demo.model.Series;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.ChapterRepository;
import com.example.demo.repository.SeriesRepository;
import com.example.demo.spec.ChapterSpecification;
import com.example.demo.spec.SeriesSpecification;
import com.example.demo.util.PaginationUtil;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Data
@Service
public class SeriesService {

    private final SeriesRepository seriesRepository;

    private final ChapterRepository chapterRepository;

    private final BookRepository bookRepository;


    public ResponseEntity<Page<Series>> getSeries(Map allParams) {
        Pageable pageRequest = PaginationUtil.extractPage(allParams);
        Specification<Series> specification = SeriesSpecification.filterSeries(allParams);
        final Page<Series> series = seriesRepository.findAll(specification, pageRequest);
        for (Series serie : series) {
            serie.setChapters(seriesRepository.findRecentChapterBySeries(serie, PageRequest.of(0, 4)));
            for (Chapter chapter : serie.getChapters()) {
                chapter.setBooks(bookRepository.findRecentBooksByChapter(chapter, PageRequest.of(0, 1)));
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(series);
    }

    public ResponseEntity<Page<Chapter>> getSeriesChapters(Map allParams, UUID seriesUUID) {
        PageRequest pageRequest = (PageRequest) PaginationUtil.extractPage(allParams);
        Page<Chapter> chapters;
        boolean hasPageParam = allParams.containsKey("page");
        boolean hasSizeParam = allParams.containsKey("size");
        Series series = seriesRepository.findSeriesBySeries_uuid(seriesUUID);
        if (hasPageParam && hasSizeParam) {
            chapters = chapterRepository.findSeriesBySeries_uuidAndChapters(series, pageRequest);
        } else {
            Specification<Chapter> baseSpecification = (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.and(criteriaBuilder.and(criteriaBuilder.equal(root.get("series"), series), criteriaBuilder.isNull(root.get("deleted_at"))));
            Specification<Chapter> chapterSpecification = ChapterSpecification.filterChapter(allParams);
            Specification<Chapter> finalSpec = baseSpecification.and(chapterSpecification);
            chapters = chapterRepository.findAll(finalSpec, pageRequest);
        }
        for (Chapter chapter : chapters) {
            chapter.setBooks(bookRepository.findRecentBooksByChapter(chapter, PageRequest.of(0, 1)));
        }
        return ResponseEntity.status(HttpStatus.OK).body(chapters);
    }
}
