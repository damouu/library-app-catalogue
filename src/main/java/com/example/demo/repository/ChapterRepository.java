package com.example.demo.repository;

import com.example.demo.model.Chapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Integer>, JpaSpecificationExecutor<Chapter> {

    Page<Chapter> findAll(Specification<Chapter> specification, Pageable pageable);

    Optional<Chapter> findByUuidAndDeletedAtIsNull(UUID chapterUUID);

    Page<Chapter> findBySeriesUuid(UUID chapterUUID, Pageable pageable);


}
