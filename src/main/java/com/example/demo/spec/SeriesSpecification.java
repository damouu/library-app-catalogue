package com.example.demo.spec;

import com.example.demo.dto.SeriesFilterDTO;
import com.example.demo.model.Series;
import org.springframework.data.jpa.domain.Specification;

public class SeriesSpecification {

    public static Specification<Series> filterSeries(SeriesFilterDTO filter) {
        return Specification
                .where(SpecificationUtils.<Series>isNull("deletedAt"))
                .and(SpecificationUtils.optionalContains("title", filter.title()))
                .and(SpecificationUtils.optionalContains("author", filter.author()))
                .and(SpecificationUtils.optionalContains("genre", filter.genre()))
                .and(SpecificationUtils.optionalContains("illustrator", filter.illustrator()))
                .and(SpecificationUtils.optionalContains("publisher", filter.publisher()));
    }

}
