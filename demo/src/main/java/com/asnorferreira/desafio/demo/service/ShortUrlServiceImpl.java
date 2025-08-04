package com.asnorferreira.desafio.demo.service;

import com.asnorferreira.desafio.demo.application.dto.ShortUrlRequest;
import com.asnorferreira.desafio.demo.application.dto.ShortUrlResponse;
import com.asnorferreira.desafio.demo.application.dto.UrlStatisticsResponse;
import com.asnorferreira.desafio.demo.domain.exception.ShortUrlNotFoundException;
import com.asnorferreira.desafio.demo.domain.model.ShortUrl;
import com.asnorferreira.desafio.demo.infrastructure.repository.ShortUrlRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Service
public class ShortUrlServiceImpl implements ShortUrlService {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int SHORT_CODE_LENGTH = 6;

    private final ShortUrlRepository repository;
    private final String baseUrl;

    public ShortUrlServiceImpl(ShortUrlRepository repository, @Value("${demo.base-url}") String baseUrl) {
        this.repository = repository;
        this.baseUrl = baseUrl;
    }

    @Override
    public ShortUrlResponse createShortUrl(ShortUrlRequest request) {
        String shortCode = generateShortCode();
        ShortUrl shortUrl = new ShortUrl(request.originalUrl(), shortCode);
        repository.save(shortUrl);
        return new ShortUrlResponse(
                baseUrl + shortCode,
                shortUrl.getOriginalUrl(),
                shortUrl.getCreatedAt().toString()
        );
    }

    @Override
    public String getOriginalUrl(String shortCode) {
        ShortUrl shortUrl = repository.findByShortCode(shortCode)
                .orElseThrow(() -> new ShortUrlNotFoundException(shortCode));
        shortUrl.recordAccess();
        repository.save(shortUrl);
        return shortUrl.getOriginalUrl();
    }

    @Override
    public UrlStatisticsResponse getStatistics(String shortCode) {
        ShortUrl shortUrl = repository.findByShortCode(shortCode)
                .orElseThrow(() -> new ShortUrlNotFoundException(shortCode));
        
        long totalAccesses = shortUrl.getAccessRecords().size();
        double averageAccessesPerDay = calculateAverageAccessesPerDay(shortUrl);
        
        return new UrlStatisticsResponse(
                baseUrl + shortCode,
                shortUrl.getOriginalUrl(),
                totalAccesses,
                averageAccessesPerDay
        );
    }

    private String generateShortCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < SHORT_CODE_LENGTH; i++) {
            code.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }

    private double calculateAverageAccessesPerDay(ShortUrl shortUrl) {
        if (shortUrl.getAccessRecords().isEmpty()) {
            return 0.0;
        }
        LocalDateTime firstAccess = shortUrl.getCreatedAt();
        LocalDateTime now = LocalDateTime.now();
        long days = ChronoUnit.DAYS.between(firstAccess, now);
        long totalAccesses = shortUrl.getAccessRecords().size();
        return days == 0 ? totalAccesses : (double) totalAccesses / days;
    }
}