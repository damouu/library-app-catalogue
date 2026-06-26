package com.example.demo.search;

import org.springframework.data.domain.Sort;

import java.util.Set;

public final class SearchWhitelistChapter {

    private SearchWhitelistChapter() {
    }

    public static final Set<String> CHAPTER_QUERY_PARAMS = Set.of(
            "title",
            "secondTitle",
            "chapterNumber",
            "publicationDate",
            "page",
            "size",
            "sort"
    );

    public static final Set<String> CHAPTER_SORT_FIELDS = Set.of(
            "title",
            "chapterNumber",
            "publicationDate"
    );

    public static void validateSort(Sort sort, Set<String> allowFields) {
        for (Sort.Order order : sort) {
            if (!allowFields.contains(order.getProperty())) {
                throw new IllegalArgumentException('\'' + order.getProperty() + "' is not allowed");
            }
        }
    }

    public static void validate(Set<String> requestParams) {
        for (String param : requestParams) {
            if (!CHAPTER_QUERY_PARAMS.contains(param)) {
                throw new IllegalArgumentException("'" + param + "' is not allowed");
            }
        }
    }

}
