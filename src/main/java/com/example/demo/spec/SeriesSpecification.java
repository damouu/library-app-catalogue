package com.example.demo.spec;

import com.example.demo.dto.SeriesFilterDTO;
import com.example.demo.model.Series;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class SeriesSpecification {

    public static Specification<Series> filterSeries(SeriesFilterDTO filter) {

        return Specification.where(optionalContains("title", filter.title()))
                .and(optionalContains("author", filter.author()))
                .and(optionalContains("genre", filter.genre()))
                .and(optionalContains("illustrator", filter.illustrator()))
                .and(optionalContains("publisher", filter.publisher()));
    }

    private static Specification<Series> optionalContains(String field, String value) {

        if (StringUtils.isBlank(value)) {
            return null;
        }
        
        return contains(field, value);
    }

    public static Specification<Series> contains(String field, String value) {
        return (root, query, cb) -> cb.like(cb.lower(root.get(field)), "%" + value.toLowerCase() + "%");
    }
}
