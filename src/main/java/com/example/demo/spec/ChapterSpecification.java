package com.example.demo.spec;

import com.example.demo.dto.ChapterFilterDTO;
import com.example.demo.model.Chapter;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.UUID;


public class ChapterSpecification {

    public static Specification<Chapter> filterChapter(ChapterFilterDTO filter) {
        return Specification.where(optionalContains("title", filter.title())).and(optionalEquals("chapterNumber", filter.chapterNumber())).and(optionalContains("secondTitle", filter.secondTitle())).and(optionalEquals("publicationDate", filter.publicationDate()));
    }

    private static Specification<Chapter> optionalContains(String field, String value) {

        if (StringUtils.isBlank(value)) {
            return null;
        }

        return contains(field, value);
    }

    private static <T> Specification<Chapter> optionalEquals(String field, T value) {

        if (value == null) {
            return null;
        }

        return (root, query, cb) -> cb.equal(root.get(field), value);
    }

    private static Specification<Chapter> contains(String field, String value) {
        return (root, query, cb) -> cb.like(cb.lower(root.get(field)), "%" + value.toLowerCase() + "%");
    }

    public static Specification<Chapter> publishedBetween(LocalDate startOfWeek, LocalDate endOfWeek) {
        return (root, query, cb) -> cb.between(root.get("publicationDate"), startOfWeek, endOfWeek);
    }


    public static Specification<Chapter> belongsToSeries(UUID seriesUUID) {
        return (root, query, cb) -> cb.equal(root.get("series").get("uuid"), seriesUUID);
    }

    public static Specification<Chapter> notDeleted() {
        return (root, query, cb) -> cb.isNull(root.get("deletedAt"));
    }
}