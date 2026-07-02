package com.example.demo.integration.controller;

import com.example.demo.controller.ChapterController;
import com.example.demo.dto.ChapterSummaryDTO;
import com.example.demo.dto.SeriesSummaryDTO;
import com.example.demo.service.ChapterService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChapterController.class)
class ChapterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChapterService chapterService;

    @Test
    @DisplayName("GET /public/chapters - Should return a list of chapters")
    void testGetChapters() throws Exception {
        SeriesSummaryDTO seriesDTO = new SeriesSummaryDTO(UUID.randomUUID(), "Naruto", "Shonen", "https://example.com/naruto.jpg", "Masashi Kishimoto", "Masashi Kishimoto", "Shueisha", LocalDate.of(1999, 9, 21), LocalDate.of(2014, 11, 10));
        ChapterSummaryDTO dto = new ChapterSummaryDTO(UUID.randomUUID(), "Naruto", "Naruto", 12, 1, "Action", "dede", null, null, seriesDTO);
        Page<ChapterSummaryDTO> page = new PageImpl<>(List.of(dto));
        when(chapterService.getChapters(any(), any())).thenReturn(page);
        mockMvc.perform(get("/public/chapters").param("title", "Naruto").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.content[0].title").value("Naruto")).andExpect(jsonPath("$.content[0].chapterNumber").value(12));
    }

    @Test
    @DisplayName("GET /public/chapters/{uuid} - Should return a specific chapter")
    void testGetChapterByUUID() throws Exception {
        SeriesSummaryDTO seriesDTO = new SeriesSummaryDTO(UUID.randomUUID(), "Naruto", "Shonen", "https://example.com/naruto.jpg", "Masashi Kishimoto", "Masashi Kishimoto", "Shueisha", LocalDate.of(1999, 9, 21), LocalDate.of(2014, 11, 10));
        ChapterSummaryDTO dto = new ChapterSummaryDTO(UUID.randomUUID(), "Naruto", "Naruto", 12, 1, "Action", "dede", LocalDate.now(), null, seriesDTO);
        when(chapterService.getChapterUUID(dto.uuid())).thenReturn(dto);
        mockMvc.perform(get("/public/chapters/{chapterUUID}", dto.uuid()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.uuid").value(dto.uuid().toString())).andExpect(jsonPath("$.title").value(dto.title())).andExpect(jsonPath("$.publicationDate").value(dto.publicationDate().toString()));
    }
}