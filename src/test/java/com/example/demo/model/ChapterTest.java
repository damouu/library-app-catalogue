package com.example.demo.model;

import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ChapterTest extends Chapter {

    @Test
    @DisplayName("Should correctly build Chapter object using Lombok")
    void shouldBuildChapter() {
        UUID chapterUuid = UUID.randomUUID();
        LocalDate now = LocalDate.now();

        Chapter chapter = Chapter.builder().uuid(chapterUuid).title("The Beginning").secondTitle("secondTitle").totalPages(50).publicationDate(now).chapterNumber(1).coverArtworkUrl("coverArtworkUrl").summary("summary").series(Instancio.create(Series.class)).deletedAt(null).createdAt(LocalDateTime.now()).build();

        assertThat(chapter.getUuid()).isEqualTo(chapterUuid);
        assertThat(chapter.getTitle()).isEqualTo("The Beginning");
        assertThat(chapter.getTotalPages()).isEqualTo(50);
        assertThat(chapter.getPublicationDate()).isEqualTo(now);
        assertThat(chapter.getCreatedAt()).isNotNull();
        assertThat(chapter.getDeletedAt()).isNull();
        assertThat(chapter.getSummary()).isNotBlank().isNotNull().isInstanceOf(String.class).isEqualTo("summary");
        assertThat(chapter.getCoverArtworkUrl()).isNotBlank().isNotNull().isInstanceOf(String.class).isEqualTo("coverArtworkUrl");
        assertThat(chapter.getChapterNumber()).isNotNull().isInstanceOf(Integer.class).isEqualTo(1);
        assertThat(chapter.getSecondTitle()).isNotNull().isInstanceOf(String.class).isEqualTo("secondTitle");
        assertThat(chapter.getSeries()).isNotNull();
    }

    @Test
    void testSetTitle() {
        Chapter chapter = new Chapter();
        String newTitle = "testting the setting annoation";
        chapter.setTitle(newTitle);
        assertThat(chapter.getTitle()).as("The title should be updated to the new value").isEqualTo(newTitle);
    }

    @Test
    void testIdGetterAndSetter() {
        Chapter chapter = new Chapter();
        Integer id = 101;
        chapter.setId(id);
        assertThat(chapter.getId()).as("The ID should be manually settable for testing purposes").isEqualTo(id);
    }
}