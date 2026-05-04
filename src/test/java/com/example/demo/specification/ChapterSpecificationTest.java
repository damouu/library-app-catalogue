package com.example.demo.specification;

import com.example.demo.model.Chapter;
import com.example.demo.repository.ChapterRepository;
import com.example.demo.spec.ChapterSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@DataJpaTest
class ChapterSpecificationTest extends ChapterSpecification {

    @Autowired
    private ChapterRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        repository.save(Chapter.builder().uuid(UUID.randomUUID()).title("The Fellowship").secondTitle("Part 1").chapterNumber(1).publicationDate(LocalDate.of(2026, 1, 1)).totalPages(50).coverArtworkUrl("url1").createdAt(LocalDateTime.now()).build());

        repository.save(Chapter.builder().uuid(UUID.randomUUID()).title("The Two Towers").secondTitle("Part 2").chapterNumber(2).publicationDate(LocalDate.of(2026, 6, 1)).totalPages(60).createdAt(LocalDateTime.now()).coverArtworkUrl("url2").build());
    }

    @Test
    @DisplayName("Filter by title: Case-insensitive and partial match")
    void testFilterByTitle() {
        Map<String, String> params = new HashMap<>();
        params.put("title", "FELLOW");

        List<Chapter> results = repository.findAll(ChapterSpecification.filterChapter(params));

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTitle()).isEqualTo("The Fellowship");
    }


    @Test
    @DisplayName("Filter by secondTitle: Case-insensitive and partial match")
    void testFilterBySecondTitle() {
        Map<String, String> params = new HashMap<>();
        params.put("secondTitle", "Part 1");

        List<Chapter> results = repository.findAll(ChapterSpecification.filterChapter(params));

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getSecondTitle()).isEqualTo("Part 1");
    }

    @Test
    @DisplayName("Filter by chapter number")
    void testFilterByChapterNumber() {
        Map<String, String> params = new HashMap<>();
        params.put("chapterNumber", "1");

        List<Chapter> results = repository.findAll(ChapterSpecification.filterChapter(params));

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTitle()).isEqualTo("The Fellowship");
        assertThat(results.get(0).getChapterNumber().intValue()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when chapterNumber is not a numeric string")
    void testFilterByInvalidChapterNumber() {
        Map<String, String> params = new HashMap<>();
        params.put("chapterNumber", "abcd");

        assertThatThrownBy(() -> repository.findAll(ChapterSpecification.filterChapter(params)))
                .isInstanceOf(org.springframework.dao.InvalidDataAccessApiUsageException.class)
                .hasRootCauseInstanceOf(IllegalArgumentException.class)
                .hasStackTraceContaining("chapter number must be a number");
    }


    @Test
    @DisplayName("Should find chapters published within a specific date range")
    void testPublishedBetweenSpecification() {
        Map<String, Map> params = new HashMap<>();
        LocalDate start = LocalDate.of(2026, 1, 1);
        LocalDate end = LocalDate.of(2026, 5, 7);

        Specification<Chapter> spec = ChapterSpecification.publishedBetween(start, end);
        List<Chapter> results = repository.findAll(spec);

        assertThat(results)
                .hasSize(1)
                .extracting(Chapter::getTitle)
                .containsExactlyInAnyOrder("The Fellowship")
                .doesNotContain("The Two Towers");
    }


}