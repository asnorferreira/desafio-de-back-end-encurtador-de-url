package com.asnorferreira.desafio.demo.repository;

import com.asnorferreira.desafio.demo.domain.model.ShortUrl;
import com.asnorferreira.desafio.demo.infrastructure.repository.ShortUrlRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Testcontainers
class ShortUrlRepositoryTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest")
            .withReuse(false);

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    @BeforeEach
    void setUp() {
        shortUrlRepository.deleteAll();
    }

    @Test
    void testSaveAndFindByShortCode() {
        ShortUrl shortUrl = new ShortUrl("https://example.com", "abc123");
        shortUrlRepository.save(shortUrl);

        Optional<ShortUrl> found = shortUrlRepository.findByShortCode("abc123");
        assertTrue(found.isPresent());
        assertEquals("abc123", found.get().getShortCode());
        assertEquals("https://example.com", found.get().getOriginalUrl());
    }

    @Test
    void testFindByShortCodeNotFound() {
        Optional<ShortUrl> found = shortUrlRepository.findByShortCode("nonexistent");
        assertTrue(found.isEmpty());
    }

    @Test
    void testRecordAccess() {
        ShortUrl shortUrl = new ShortUrl("https://example.com", "abc123");
        shortUrlRepository.save(shortUrl);
        shortUrl.recordAccess();
        shortUrlRepository.save(shortUrl);

        Optional<ShortUrl> updated = shortUrlRepository.findByShortCode("abc123");
        assertTrue(updated.isPresent());
        assertEquals(1, updated.get().getAccessRecords().size());
        assertNotNull(updated.get().getAccessRecords().get(0).getAccessTime());
    }
}