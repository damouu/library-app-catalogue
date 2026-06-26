package com.example.demo.spec;

import com.example.demo.dto.ChapterFilterDTO;
import com.example.demo.model.Chapter;
import org.springframework.data.jpa.domain.Specification;


public class ChapterSpecification {

    public static Specification<Chapter> filterChapter(ChapterFilterDTO filter) {
        return Specification
                .where(SpecificationUtils.<Chapter>isNull("deletedAt"))
                .and(SpecificationUtils.optionalContains("title", filter.title()))
                .and(SpecificationUtils.optionalEquals("chapterNumber", filter.chapterNumber()))
                .and(SpecificationUtils.optionalContains("secondTitle", filter.secondTitle()))
                .and(SpecificationUtils.optionalEquals("publicationDate", filter.publicationDate()));
    }
}