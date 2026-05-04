package com.example.demo.specification;

import com.example.demo.model.Series;
import com.example.demo.repository.SeriesRepository;
import com.example.demo.spec.SeriesSpecification;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
class SeriesSpecificationTest extends SeriesSpecification {

    @Autowired
    private SeriesRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        repository.save(Series.builder().uuid(UUID.randomUUID()).title("Naruto").genre("Action, Adventure, Fantasy").illustrator("Masashi Kishimoto").publisher("Shueisha").coverArtworkUrl("dede").firstPrintPublicationDate(LocalDate.of(1999, 9, 21)).lastPrintPublicationDate(LocalDate.of(2014, 11, 10)).author("Masashi Kishimoto").createdAt(LocalDateTime.now()).build());

    }

    @Test
    @DisplayName("Filter by title: Case-insensitive and partial match")
    void testFilterByTitle() {
        Map<String, String> params = new HashMap<>();
        params.put("title", "Naruto");

        List<Series> results = repository.findAll(SeriesSpecification.filterSeries(params));

        assertThat(results).hasSize(1);
        AssertionsForClassTypes.assertThat(results.get(0).getTitle()).isEqualTo("Naruto");
    }

    @Test
    @DisplayName("Filter by illustrator: Case-insensitive and partial match")
    void testFilterByIllustrator() {
        Map<String, String> params = new HashMap<>();
        params.put("illustrator", "Masashi Kishimoto");

        List<Series> results = repository.findAll(SeriesSpecification.filterSeries(params));

        assertThat(results).hasSize(1);
        AssertionsForClassTypes.assertThat(results.get(0).getTitle()).isEqualTo("Naruto");
    }

    @Test
    @DisplayName("Filter by author: Case-insensitive and partial match")
    void testFilterByAuthor() {
        Map<String, String> params = new HashMap<>();
        params.put("author", "Masashi Kishimoto");

        List<Series> results = repository.findAll(SeriesSpecification.filterSeries(params));

        assertThat(results).hasSize(1);
        AssertionsForClassTypes.assertThat(results.get(0).getTitle()).isEqualTo("Naruto");
    }

    @Test
    @DisplayName("Filter by genre: Case-insensitive and partial match")
    void testFilterByGenre() {
        Map<String, String> params = new HashMap<>();
        params.put("genre", "Action, Adventure, Fantasy");

        List<Series> results = repository.findAll(SeriesSpecification.filterSeries(params));

        assertThat(results).hasSize(1);
        AssertionsForClassTypes.assertThat(results.get(0).getTitle()).isEqualTo("Naruto");
    }

    @Test
    @DisplayName("Filter by publisher: Case-insensitive and partial match")
    void testFilterByPublisher() {
        Map<String, String> params = new HashMap<>();
        params.put("publisher", "Shueisha");

        List<Series> results = repository.findAll(SeriesSpecification.filterSeries(params));

        assertThat(results).hasSize(1);
        AssertionsForClassTypes.assertThat(results.get(0).getTitle()).isEqualTo("Naruto");
    }

    @Test
    @DisplayName("pagination: Case-insensitive and partial match")
    void testPagination() {
        Map<String, Integer> params = new HashMap<>();
        params.put("page", 1);
        params.put("size", 1);

        List<Series> results = repository.findAll(SeriesSpecification.filterSeries(params));

        assertThat(results).hasSize(1);
        AssertionsForClassTypes.assertThat(results.get(0).getTitle()).isEqualTo("Naruto");
    }

}