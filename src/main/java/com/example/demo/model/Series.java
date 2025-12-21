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

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate lastPrintPublicationDate;

    @Column(nullable = false)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate firstPrintPublicationDate;

    @Column(nullable = false)
    private String author;

    @Column(columnDefinition = "timestamp")
    private LocalDate deletedAt;

    @Column(nullable = false, columnDefinition = "timestamp")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonIgnore
    private LocalDate createdAt;

    @OneToMany(mappedBy = "series", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private List<Chapter> chapters;

}
