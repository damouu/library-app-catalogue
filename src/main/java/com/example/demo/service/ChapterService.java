package com.example.demo.service;

import com.example.demo.model.Chapter;
import com.example.demo.repository.ChapterRepository;
import com.example.demo.spec.ChapterSpecification;
import com.example.demo.util.PaginationUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;


@Data
@Service
@RequiredArgsConstructor
public class ChapterService {

    private final ChapterRepository chapterRepository;

    public ResponseEntity<?> getChapters(Map allParams) {
        LocalDate startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = LocalDate.now().with(DayOfWeek.SUNDAY);
        Pageable pageable = PaginationUtil.extractPage(allParams);
        String typeValue = Optional.ofNullable(allParams.get("type")).map(Object::toString).orElse(null);
        switch (typeValue) {
            case ("recent"):
                Specification<Chapter> specification = ChapterSpecification.publishedBetween(startOfWeek, endOfWeek);
                Page<Chapter> chapters = chapterRepository.findAll(specification, pageable);
                return ResponseEntity.status(HttpStatus.OK).body(chapters);
            case null:
                specification = ChapterSpecification.filterChapter(allParams);
                chapters = chapterRepository.findAll(specification, pageable);
                return ResponseEntity.status(HttpStatus.OK).body(chapters);
            default:
                throw new IllegalStateException("Unexpected value: " + typeValue);
        }
    }

    public List<Chapter> getChapterTOP(List<UUID> chaptersUUID) throws ResponseStatusException {
        List<Chapter> chapters = chapterRepository.findByChapterUUIDIn(chaptersUUID);
        return ResponseEntity.ok(chapters).getBody();
    }

    public HashMap<String, List<Chapter>> getChapters(List<UUID> chaptersUUID) {
        List<Chapter> chapters = chapterRepository.findByChapterUUIDInAndDeletedATIsNull(chaptersUUID);
        if (chapters.size() != chaptersUUID.size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "One or more chapters do not exist or are deleted. Missing UUIDs");
        }
        HashMap<String, List<Chapter>> capitalCities = new HashMap<>();
        capitalCities.put("chapters", chapters);
        return ResponseEntity.ok(capitalCities).getBody();
    }


    public Chapter getChapterUUID(UUID chapterUUID) throws ResponseStatusException {
        Optional<Chapter> chapter = Optional.ofNullable(chapterRepository.findByChapterUUIDAndDeletedATIsNull(chapterUUID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "chapter does not exist")));
        return ResponseEntity.status(HttpStatus.OK).body(chapter.get()).getBody();
    }
}
