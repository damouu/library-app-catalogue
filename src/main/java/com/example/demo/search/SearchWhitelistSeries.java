package com.example.demo.search;

import org.springframework.data.domain.Sort;

import java.util.Set;

public final class SearchWhitelistSeries {

    private SearchWhitelistSeries() {
    }

    public static final Set<String> SERIES_QUERY_PARAMS = Set.of(
            "title",
            "author",
            "genre",
            "illustrator",
            "publisher",
            "firstPrintPublicationDate",
            "page",
            "size",
            "sort"
    );

    public static final Set<String> SERIES_SORT_FIELDS = Set.of(
            "title",
            "author",
            "genre",
            "firstPrintPublicationDate",
            "illustrator"
    );

    public static void validateSort(Sort sort, Set<String> allowFields) {
        for (Sort.Order order : sort) {
            if (!allowFields.contains(order.getProperty())) {
                throw new IllegalArgumentException('\'' + order.getProperty() + "' is not allowed");
            }
        }
    }

    public static void validate(Set<String> allowFields) {
        for (String allowField : allowFields) {
            if (!SERIES_QUERY_PARAMS.contains(allowField)) {
                throw new IllegalArgumentException('\'' + allowField + "' is not allowed");
            }
        }
    }

}
