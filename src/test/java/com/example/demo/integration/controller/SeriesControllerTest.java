package com.example.demo.integration.controller;

import com.example.demo.controller.SeriesController;
import com.example.demo.dto.ChapterSummaryDTO;
import com.example.demo.dto.SeriesSummaryDTO;
import com.example.demo.service.SeriesService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SeriesController.class)
class SeriesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SeriesService seriesService;

    @Test
    @DisplayName("GET /public/series - Should return a list of series")
    void testGetSeries() throws Exception {
        SeriesSummaryDTO dto = new SeriesSummaryDTO(UUID.randomUUID(), "Naruto", "Action", "cover.jpg", "dede", "dede", "dede");
        Page<SeriesSummaryDTO> page = new PageImpl<>(List.of(dto));
        when(seriesService.getSeries(any(), any())).thenReturn(page);
        mockMvc.perform(get("/public/series").param("title", "Naruto").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.content[0].title").value("Naruto"));
    }


    @Test
    @DisplayName("GET /public/series/{uuid}/chapters - Should return paginated chapters")
    void testGetSeriesChapters() throws Exception {
        UUID seriesUUID = UUID.randomUUID();
        ChapterSummaryDTO dto1 = new ChapterSummaryDTO(UUID.randomUUID(), "Chapter 1", "Chapter 1", 1, 1, "dede", "dede", "dede", "dede", UUID.randomUUID());
        ChapterSummaryDTO dto2 = new ChapterSummaryDTO(UUID.randomUUID(), "Chapter 2", "Chapter 2", 2, 2, "dede", "dede", "dede", "dede", UUID.randomUUID());
        Page<ChapterSummaryDTO> page = new PageImpl<>(List.of(dto1, dto2), PageRequest.of(0, 10), 2);
        when(seriesService.getSeriesChapters(eq(seriesUUID), any(Pageable.class))).thenReturn(page);
        mockMvc.perform(get("/public/series/{seriesUUID}/chapters", seriesUUID).param("page", "0").param("size", "10").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.content[0].title").value("Chapter 1")).andExpect(jsonPath("$.content[1].title").value("Chapter 2")).andExpect(jsonPath("$.totalElements").value(2)).andExpect(jsonPath("$.size").value(10)).andExpect(jsonPath("$.number").value(0));
    }

}