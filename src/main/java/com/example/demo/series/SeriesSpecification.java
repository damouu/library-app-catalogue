package com.example.demo.series;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.Map;

public class SeriesSpecification {
    public static Specification<Series> filterSeries(Map allParams) {
        return (root, query, criteriaBuilder) -> {
            Predicate titlePredicate = criteriaBuilder.like(root.get("title"), StringUtils.isBlank((String) allParams.get("title")) ? likePattern("") : (String) allParams.get("title"));
            Predicate authorPredicate = criteriaBuilder.like(root.get("author"), StringUtils.isBlank((String) allParams.get("author")) ? likePattern("") : (String) allParams.get("author"));
            Predicate genrePredicate = criteriaBuilder.like(root.get("genre"), StringUtils.isBlank((String) allParams.get("genre")) ? likePattern("") : (String) allParams.get("genre"));
            Predicate publisherPredicate = criteriaBuilder.like(root.get("publisher"), StringUtils.isBlank((String) allParams.get("publisher")) ? likePattern("") : (String) allParams.get("publisher"));
            return criteriaBuilder.and(titlePredicate, genrePredicate, authorPredicate, publisherPredicate);
        };
    }


    public static Specification<Series> searchSeries(String search) {
        return (root, query, criteriaBuilder) -> {
            Predicate titlePredicate = criteriaBuilder.like(root.get("genre"), likePattern(search));
            return criteriaBuilder.or(titlePredicate);
        };
    }


    private static String likePattern(String value) {
        return "%" + value + "%";
    }
}
