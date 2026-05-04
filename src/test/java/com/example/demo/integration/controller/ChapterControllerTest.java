package com.example.demo.integration.controller;

import com.example.demo.controller.ChapterController;
import com.example.demo.model.Chapter;
import com.example.demo.service.ChapterService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
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
        Chapter mockChapter = Chapter.builder()
                .title("Naruto Uzumaki")
                .chapterNumber(1)
                .build();

        doReturn(ResponseEntity.ok(List.of(mockChapter)))
                .when(chapterService).getChapters(any());

        mockMvc.perform(get("/public/chapters")
                        .param("title", "Naruto")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Expect HTTP 200
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("Naruto Uzumaki"))
                .andExpect(jsonPath("$[0].chapterNumber").value(1));
    }

    @Test
    @DisplayName("GET /public/chapters/{uuid} - Should return a specific chapter")
    void testGetChapterByUUID() throws Exception {
        UUID targetUuid = UUID.randomUUID();
        Chapter mockChapter = Chapter.builder()
                .uuid(targetUuid)
                .title("The Worst Generation")
                .publicationDate(LocalDate.of(2008, 10, 6))
                .build();

        when(chapterService.getChapterUUID(eq(targetUuid)))
                .thenReturn(mockChapter);

        mockMvc.perform(get("/public/chapters/{chapterUUID}", targetUuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(targetUuid.toString()))
                .andExpect(jsonPath("$.title").value("The Worst Generation"))
                .andExpect(jsonPath("$.publicationDate").value("2008-10-06"));
    }
}