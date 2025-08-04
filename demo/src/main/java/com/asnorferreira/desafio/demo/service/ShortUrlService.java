package com.asnorferreira.desafio.demo.service;

import com.asnorferreira.desafio.demo.application.dto.ShortUrlRequest;
import com.asnorferreira.desafio.demo.application.dto.ShortUrlResponse;
import com.asnorferreira.desafio.demo.application.dto.UrlStatisticsResponse;

public interface ShortUrlService {
    ShortUrlResponse createShortUrl(ShortUrlRequest request);
    String getOriginalUrl(String shortCode);
    UrlStatisticsResponse getStatistics(String shortCode);
}