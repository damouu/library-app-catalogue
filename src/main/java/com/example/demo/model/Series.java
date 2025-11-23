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
@Entity(name = "series")
@Table(name = "series", uniqueConstraints = {@UniqueConstraint(name = "series_uuid", columnNames = "series_uuid")})
@NoArgsConstructor
public class Series {
    @Id
    @SequenceGenerator(name = "series_sequence", allocationSize = 1, sequenceName = "series_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "series_sequence")
    @Column(updatable = false, nullable = false)
    @Getter(onMethod = @__(@JsonIgnore))
    @Setter
    private Integer id;

    @Column(nullable = false, columnDefinition = "UUID", name = "series_uuid")
    @Getter
    @Setter
    private UUID seriesUUID;

    @Column(name = "title", nullable = false)
    @Getter
    @Setter
    private String title;

    @Column(name = "genre", nullable = false)
    @Getter
    @Setter
    private String genre;

    @Column(name = "cover_artwork_URL", nullable = false)
    @Getter
    @Setter
    private String coverArtworkURL;

    @Column(name = "illustrator", nullable = false)
    @Getter
    @Setter
    private String illustrator;

    @Column(name = "publisher", nullable = false)
    @Getter
    @Setter
    private String publisher;

    @Column(name = "last_print_publication_date", columnDefinition = "date")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Getter
    @Setter
    private LocalDate lastPrintPublicationDate;

    @Column(name = "first_print_publication_date", nullable = false, columnDefinition = "date")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @NotNull
    @Getter
    @Setter
    private LocalDate firstPrintPublicationDate;

    @Column(name = "author", nullable = false)
    @Getter
    @Setter
    private String author;

    @Column(name = "deleted_at", columnDefinition = "timestamp")
    @Getter
    @Setter
    private LocalDate deleted_at;

    @Column(name = "created_at", nullable = false, columnDefinition = "timestamp")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @NotNull
    @Getter(onMethod = @__(@JsonIgnore))
    @Setter
    @JsonIgnore
    private LocalDate createdAt;

    @OneToMany(mappedBy = "series", fetch = FetchType.EAGER)
    @Getter
    @Setter
    private List<Chapter> chapters;

    @JsonCreator
    public Series(Integer id, UUID series_uuid, String title, String genre, String cover_artwork_URL, String illustrator, String publisher, LocalDate last_print_publication_date, LocalDate first_print_publication_date, String author, LocalDate deleted_at, LocalDate created_at) {
        this.id = id;
        this.seriesUUID = series_uuid;
        this.title = title;
        this.genre = genre;
        this.coverArtworkURL = cover_artwork_URL;
        this.illustrator = illustrator;
        this.publisher = publisher;
        this.lastPrintPublicationDate = last_print_publication_date;
        this.firstPrintPublicationDate = first_print_publication_date;
        this.author = author;
        this.deleted_at = deleted_at;
        this.createdAt = created_at;
    }

}
