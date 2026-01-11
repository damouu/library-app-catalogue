package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Getter
@Setter
public class Chapter {
    @Id
    @SequenceGenerator(name = "chapter_sequence", allocationSize = 1, sequenceName = "chapter_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chapter_sequence")
    @JsonIgnore
    @Column(updatable = false, nullable = false)
    private Integer id;

    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String secondTitle;

    @Column(nullable = false)
    private Integer totalPages;

    @Column(nullable = false)
    private Integer chapterNumber;

    @Column(nullable = false)
    private String coverArtworkUrl;

    @Column(nullable = false)
    private LocalDate publicationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id", referencedColumnName = "id")
    @JsonIgnore
    private Series series;

    @Column(columnDefinition = "timestamp")
    private LocalDateTime deletedAt;

    @Column(nullable = false, columnDefinition = "timestamp")
    @JsonIgnore
    private LocalDateTime createdAt;

}
