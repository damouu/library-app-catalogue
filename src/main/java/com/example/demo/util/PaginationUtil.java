package com.example.demo.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Map;

public class PaginationUtil {

    public static Pageable extractPage(Map<String, String> allParams) {

        PageRequest pageable;

        int page = Integer.parseInt(allParams.getOrDefault("page", "0"));
        int size = Integer.parseInt(allParams.getOrDefault("size", "10"));
        String sortField = allParams.getOrDefault("sort", "publicationDate");
        String sortDirection = allParams.getOrDefault("direction", "desc");

        Sort sort = sortDirection.equalsIgnoreCase("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        pageable = PageRequest.of(page, size, sort);
        return pageable;
    }
}
