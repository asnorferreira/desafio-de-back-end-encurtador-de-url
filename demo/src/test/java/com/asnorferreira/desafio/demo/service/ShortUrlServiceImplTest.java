package com.asnorferreira.desafio.demo.service;

import com.asnorferreira.desafio.demo.application.dto.ShortUrlRequest;
import com.asnorferreira.desafio.demo.application.dto.ShortUrlResponse;
import com.asnorferreira.desafio.demo.application.dto.UrlStatisticsResponse;
import com.asnorferreira.desafio.demo.domain.exception.ShortUrlNotFoundException;
import com.asnorferreira.desafio.demo.domain.model.ShortUrl;
import com.asnorferreira.desafio.demo.infrastructure.repository.ShortUrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class ShortUrlServiceImplTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest")
            .withReuse(false);

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private ShortUrlRepository repository;

    @Autowired
    private ShortUrlServiceImpl shortUrlService;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void testCreateShortUrl() {
        ShortUrlRequest request = new ShortUrlRequest("https://example.com");
        ShortUrlResponse response = shortUrlService.createShortUrl(request);

        assertNotNull(response);
        assertTrue(response.shortUrl().startsWith("http://localhost:8080/"));
        assertEquals(request.originalUrl(), response.originalUrl());

        Optional<ShortUrl> found = repository.findByShortCode(response.shortUrl().replace("http://localhost:8080/", ""));
        assertTrue(found.isPresent());
    }

    @Test
    void testGetOriginalUrl() {
        ShortUrl shortUrl = new ShortUrl("https://example.com", "abc123");
        repository.save(shortUrl);

        String originalUrl = shortUrlService.getOriginalUrl("abc123");

        assertEquals("https://example.com", originalUrl);
    }

    @Test
    void testGetOriginalUrlNotFound() {
        assertThrows(ShortUrlNotFoundException.class, () -> shortUrlService.getOriginalUrl("notfound"));
    }

    @Test
    void testGetStatistics() {
        ShortUrl shortUrl = new ShortUrl("https://example.com", "abc123");
        shortUrl.recordAccess();
        repository.save(shortUrl);

        UrlStatisticsResponse response = shortUrlService.getStatistics("abc123");

        assertNotNull(response);
        assertEquals("http://localhost:8080/abc123", response.shortUrl());
        assertEquals(1, response.totalAccesses());
        assertTrue(response.averageAccessesPerDay() >= 0.0);
    }
}