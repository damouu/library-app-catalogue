package com.example.demo.integration.controller;

import com.example.demo.exception.ChapterAlreadyRegisteredException;
import com.example.demo.exception.ChapterNotFoundException;
import com.example.demo.exception.SeriesAlreadyRegisteredException;
import com.example.demo.exception.SeriesNotFoundException;
import com.example.demo.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TestExceptionController.class)
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @RestController
    static class TestController {

        @GetMapping("/test/chapter/conflict")
        void throwChapterConflict() {
            throw new ChapterAlreadyRegisteredException(1);
        }

        @GetMapping("/test/series/conflict")
        void throwSeriesConflict() {
            throw new SeriesAlreadyRegisteredException("naruto");
        }

        @GetMapping("/test/series/notfound")
        void throwSeriesNotFound() {
            throw new SeriesNotFoundException(UUID.randomUUID());
        }

        @GetMapping("/test/chapter/notfound")
        void throwChapterNotFound() {
            throw new ChapterNotFoundException(UUID.randomUUID());
        }

    }

    @Test
    void should_return_conflict_when_chapter_already_registered() throws Exception {
        mockMvc.perform(get("/test/chapter/conflict")).andExpect(status().isConflict());
    }

    @Test
    void should_return_conflict_when_series_already_registered() throws Exception {
        mockMvc.perform(get("/test/series/conflict")).andExpect(status().isConflict());
    }

    @Test
    void should_return_not_found_when_series_not_found() throws Exception {
        mockMvc.perform(get("/test/series/notfound")).andExpect(status().isNotFound());
    }

    @Test
    void should_return_not_found_when_chapter_not_found() throws Exception {
        mockMvc.perform(get("/test/chapter/notfound")).andExpect(status().isNotFound());
    }
}