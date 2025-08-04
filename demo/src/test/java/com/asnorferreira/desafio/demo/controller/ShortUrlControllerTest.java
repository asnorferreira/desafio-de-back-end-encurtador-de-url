package com.asnorferreira.desafio.demo.controller;

import com.asnorferreira.desafio.demo.application.controller.ShortUrlController;
import com.asnorferreira.desafio.demo.application.dto.ShortUrlRequest;
import com.asnorferreira.desafio.demo.application.dto.ShortUrlResponse;
import com.asnorferreira.desafio.demo.application.dto.UrlStatisticsResponse;
import com.asnorferreira.desafio.demo.service.ShortUrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ShortUrlControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ShortUrlService shortUrlService;

    @InjectMocks
    private ShortUrlController shortUrlController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(shortUrlController).build();
    }

    @Test
    void testCreateShortUrl() throws Exception {
        when(shortUrlService.createShortUrl(any(ShortUrlRequest.class)))
                .thenReturn(new ShortUrlResponse("http://localhost:8080/abc123", "https://example.com", "2025-08-03T23:39:00"));

        mockMvc.perform(post("/api/v1/urls")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"originalUrl\":\"https://example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shortUrl").value("http://localhost:8080/abc123"))
                .andExpect(jsonPath("$.originalUrl").value("https://example.com"));
    }

    @Test
    void testRedirectToOriginalUrl() throws Exception {
        when(shortUrlService.getOriginalUrl("abc123")).thenReturn("https://example.com");

        mockMvc.perform(get("/api/v1/urls/abc123"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "https://example.com"));
    }

    @Test
    void testGetStatistics() throws Exception {
        when(shortUrlService.getStatistics("abc123"))
                .thenReturn(new UrlStatisticsResponse("http://localhost:8080/abc123", "https://example.com", 10, 1.0));

        mockMvc.perform(get("/api/v1/urls/abc123/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shortUrl").value("http://localhost:8080/abc123"))
                .andExpect(jsonPath("$.totalAccesses").value(10))
                .andExpect(jsonPath("$.averageAccessesPerDay").value(1.0));
    }

    @Test
    void testCreateShortUrlInvalidInput() throws Exception {
        mockMvc.perform(post("/api/v1/urls")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"originalUrl\":\"invalid-url\"}"))
                .andExpect(status().isBadRequest());
    }
}
