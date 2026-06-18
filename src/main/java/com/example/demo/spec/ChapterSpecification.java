package com.example.demo.spec;

import com.example.demo.model.Chapter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ChapterSpecification {

    public static Specification<Chapter> filterChapter(Map allParams) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();
            String chapterNumberStr = (String) allParams.get("chapterNumber");
            String title = (String) allParams.get("title");
            String secondTitle = (String) allParams.get("secondTitle");

            if (StringUtils.isNotBlank(title)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), likePattern(title)));
            }

            if (StringUtils.isNotBlank(secondTitle)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("secondTitle")), likePattern(secondTitle)));
            }

            if (org.apache.commons.lang3.StringUtils.isNotBlank(chapterNumberStr)) {

                try {

                    Integer chapterNumberInt = Integer.parseInt(chapterNumberStr);
                    predicates.add(criteriaBuilder.equal(root.get("chapterNumber"), chapterNumberInt));

                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("chapter number must be a number", e);
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Chapter> publishedBetween(LocalDate startOfWeek, LocalDate endOfWeek) {
        return (root, query, cb) -> cb.between(root.get("publicationDate"), startOfWeek, endOfWeek);
    }

    public static Specification<Chapter> hasType(String type) {
        return (root, query, cb) -> type != null ? cb.equal(root.get("type"), type) : cb.conjunction();
    }

    private static String likePattern(String value) {
        return "%" + value.toLowerCase() + "%";
    }

    public static Specification<Chapter> belongsToSeries(UUID seriesUUID) {
        return (root, query, cb) -> cb.equal(root.get("series").get("uuid"), seriesUUID);
    }

    public static Specification<Chapter> notDeleted() {
        return (root, query, cb) -> cb.isNull(root.get("deletedAt"));
    }
}