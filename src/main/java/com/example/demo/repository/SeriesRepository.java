package com.example.demo.repository;

import com.example.demo.model.Series;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SeriesRepository extends JpaRepository<Series, Integer>, JpaSpecificationExecutor<Series> {

    Page<Series> findAll(Specification<Series> specification, Pageable pageable);

    Optional<Series> findByUuidAndDeletedAtIsNull(UUID seriesUUID);

}
