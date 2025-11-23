package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(name = "chapter")
@Table(name = "chapter", uniqueConstraints = {@UniqueConstraint(name = "chapter_uuid", columnNames = "chapter_uuid")})
@NoArgsConstructor
public class Chapter {
    @Id
    @SequenceGenerator(name = "chapter_sequence", allocationSize = 1, sequenceName = "chapter_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chapter_sequence")
    @Column(updatable = false, nullable = false)
    @Getter(onMethod = @__(@JsonIgnore))
    @Setter
    private Integer id;

    @Column(nullable = false, columnDefinition = "UUID", name = "chapter_uuid")
    @Getter
    @Setter
    private UUID chapterUUID;

    @Column(name = "title", nullable = false)
    @Getter
    @Setter
    private String title;

    @Column(name = "second_title", nullable = false)
    @Getter
    @Setter
    private String secondTitle;

    @Column(name = "total_pages", nullable = false)
    @Getter
    @Setter
    private Integer totalPages;

    @Column(name = "chapter_number", nullable = false)
    @Getter
    @Setter
    private Integer chapterNumber;

    @Column(name = "cover_artwork_URL", nullable = false)
    @Getter
    @Setter
    private String coverArtworkURL;

    @Column(name = "publication_date", nullable = false, columnDefinition = "Date")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @NotNull
    @Getter
    @Setter
    private LocalDate publicationDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "series_id", referencedColumnName = "id")
    @JsonIgnore
    private Series series;

    @OneToMany(mappedBy = "chapter", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    @Setter
    private List<Book> books;

    @Column(name = "deleted_at", columnDefinition = "timestamp")
    @Getter
    @Setter
    private LocalDate deleted_at;

    @Column(name = "created_at", nullable = false, columnDefinition = "timestamp")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Getter(onMethod = @__(@JsonIgnore))
    @Setter
    @JsonIgnore
    private LocalDate createdAT;

    @JsonCreator
    public Chapter(Integer id, UUID chapter_uuid, String title, String second_title, Integer totalPages, Integer chapter_number, String cover_artwork_URL, LocalDate publication_date, Series series, LocalDate deleted_at, LocalDate created_at) {
        this.id = id;
        this.chapterUUID = chapter_uuid;
        this.title = title;
        this.secondTitle = second_title;
        this.totalPages = totalPages;
        this.chapterNumber = chapter_number;
        this.coverArtworkURL = cover_artwork_URL;
        this.publicationDate = publication_date;
        this.series = series;
        this.deleted_at = deleted_at;
        this.createdAT = created_at;
    }
}
