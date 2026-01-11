package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
@Setter
public class Series {
    @Id
    @SequenceGenerator(name = "series_sequence", allocationSize = 1, sequenceName = "series_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "series_sequence")
    @Column(updatable = false, nullable = false)
    @JsonIgnore
    private Integer id;

    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String genre;

    @Column(nullable = false)
    private String coverArtworkUrl;

    @Column(nullable = false)
    private String illustrator;

    @Column(nullable = false)
    private String publisher;

    private LocalDate lastPrintPublicationDate;

    @Column(nullable = false)
    private LocalDate firstPrintPublicationDate;

    @Column(nullable = false)
    private String author;

    @Column(columnDefinition = "timestamp")
    private LocalDateTime deletedAt;

    @Column(nullable = false, columnDefinition = "timestamp")
    @JsonIgnore
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "series", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Chapter> chapters;

}
