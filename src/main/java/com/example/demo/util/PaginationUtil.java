package com.example.demo.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Map;

public class PaginationUtil {

    public static Pageable extractPage(Map<String, String> allParams) {

        PageRequest pageable = null;

        if (allParams.size() == 2 && allParams.containsKey("page") && allParams.containsKey("size")) {
            pageable = PageRequest.of(Integer.parseInt(allParams.get("page")), Integer.parseInt(allParams.get("size")));
        } else if (allParams.size() > 2 && allParams.containsKey("page") && allParams.containsKey("size")) {
            pageable = PageRequest.of(Integer.parseInt(allParams.get("page")), Integer.parseInt(allParams.get("size")), Sort.unsorted());
        } else {
            pageable = PageRequest.of(0, 20);
        }
        return pageable;
    }
}
