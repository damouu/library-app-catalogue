package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(name = "chapter")
@Table(name = "chapter", uniqueConstraints = {@UniqueConstraint(name = "chapter_uuid", columnNames = "chapter_uuid")})
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(nullable = false, columnDefinition = "UUID", name = "chapter_uuid")
    private UUID chapterUUID;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "second_title", nullable = false)
    private String secondTitle;

    @Column(name = "total_pages", nullable = false)
    private Integer totalPages;

    @Column(name = "chapter_number", nullable = false)
    private Integer chapterNumber;

    @Column(name = "cover_artwork_URL", nullable = false)
    private String coverArtworkURL;

    @Column(name = "publication_date", nullable = false, columnDefinition = "Date")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate publicationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id", referencedColumnName = "id")
    @JsonIgnore
    @ToString.Exclude
    private Series series;

    @Column(name = "deleted_at", columnDefinition = "timestamp")
    private LocalDate deletedAT;

    @Column(name = "created_at", nullable = false, columnDefinition = "timestamp")
    @JsonIgnore
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate createdAT;

}
