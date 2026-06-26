package com.example.demo.spec;

import com.example.demo.model.Chapter;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.UUID;

public final class SpecificationUtils {

    private SpecificationUtils() {
    }

    public static <T> Specification<T> optionalContains(String field, String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return contains(field, value);
    }

    public static <T, V> Specification<T> optionalEquals(String field, V value) {
        if (value == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get(field), value);
    }

    public static <T> Specification<T> contains(String field, String value) {
        return (root, query, cb) -> cb.like(cb.lower(root.get(field)), "%" + value.toLowerCase() + "%");
    }

    public static <T> Specification<T> isNull(String field) {
        return (root, query, cb) -> cb.isNull(root.get(field));
    }

    public static <T> Specification<T> isNotNull(String field) {
        return (root, query, cb) -> cb.isNotNull(root.get(field));
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