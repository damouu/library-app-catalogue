package com.example.demo.specification;

import com.example.demo.dto.SeriesFilterDTO;
import com.example.demo.model.Series;
import com.example.demo.repository.SeriesRepository;
import com.example.demo.spec.SeriesSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
    @DisplayName("Filter by title")
    void testFilterByTitle() {
        SeriesFilterDTO filter = new SeriesFilterDTO("Naruto", null, null, null, null);
        List<Series> results = repository.findAll(SeriesSpecification.filterSeries(filter));
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTitle()).isEqualTo("Naruto");
    }

    @Test
    void testFilterByAuthor() {
        SeriesFilterDTO filter = new SeriesFilterDTO(null, "Masashi Kishimoto", null, null, null);
        List<Series> results = repository.findAll(SeriesSpecification.filterSeries(filter));
        assertThat(results).hasSize(1);
    }

    @Test
    void testFilterByIllustrator() {
        SeriesFilterDTO filter = new SeriesFilterDTO(null, null, "Masashi Kishimoto", null, null);
        List<Series> results = repository.findAll(SeriesSpecification.filterSeries(filter));
        assertThat(results).hasSize(1);
    }

    @Test
    void testFilterByGenre() {
        SeriesFilterDTO filter = new SeriesFilterDTO(null, null, null, "Action", null);
        List<Series> results = repository.findAll(SeriesSpecification.filterSeries(filter));
        assertThat(results).hasSize(1);
    }

    @Test
    void testFilterByPublisher() {
        SeriesFilterDTO filter = new SeriesFilterDTO(null, null, null, null, "Shueisha");
        List<Series> results = repository.findAll(SeriesSpecification.filterSeries(filter));
        assertThat(results).hasSize(1);
    }

}