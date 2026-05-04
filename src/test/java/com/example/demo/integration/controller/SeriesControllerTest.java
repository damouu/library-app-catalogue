package com.example.demo.integration.controller;

import com.example.demo.controller.SeriesController;
import com.example.demo.model.Chapter;
import com.example.demo.model.Series;
import com.example.demo.service.SeriesService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SeriesController.class)
class SeriesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SeriesService seriesService;

    @Test
    @DisplayName("GET /public/series - Should return a list of series")
    void testGetSeries() throws Exception {
        Series mockSeries = Series.builder()
                .uuid(UUID.randomUUID())
                .title("Naruto")
                .genre("Action, Adventure, Fantasy")
                .illustrator("Masashi Kishimoto")
                .publisher("Shueisha")
                .firstPrintPublicationDate(LocalDate.of(1999, 9, 21))
                .lastPrintPublicationDate(LocalDate.of(2014, 11, 10))
                .author("Masashi Kishimoto")
                .createdAt(LocalDateTime.now())
                .build();

        doReturn(ResponseEntity.ok(List.of(mockSeries)))
                .when(seriesService).getSeries(any());

        mockMvc.perform(get("/public/series")
                        .param("title", "Naruto")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("Naruto"));
    }


    @Test
    @DisplayName("GET /public/series/{uuid}/chapters - Should return paginated chapters")
    void testGetSeriesChapters() throws Exception {
        UUID seriesUUID = UUID.randomUUID();

        Chapter chapter1 = Chapter.builder()
                .uuid(UUID.randomUUID())
                .title("Chapter 1")
                .publicationDate(LocalDate.of(2024, 1, 1))
                .build();

        Chapter chapter2 = Chapter.builder()
                .uuid(UUID.randomUUID())
                .title("Chapter 2")
                .publicationDate(LocalDate.of(2024, 1, 2))
                .build();

        Page<Chapter> mockPage = new PageImpl<>(
                List.of(chapter1, chapter2),
                PageRequest.of(0, 10),
                2
        );

        when(seriesService.getSeriesChapters(anyMap(), eq(seriesUUID)))
                .thenReturn(ResponseEntity.ok(mockPage));

        mockMvc.perform(get("/public/series/{seriesUUID}/chapters", seriesUUID)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Chapter 1"))
                .andExpect(jsonPath("$.content[1].title").value("Chapter 2"))
                .andExpect(jsonPath("$.content[0].publicationDate").value("2024-01-01"))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.number").value(0));
    }

}