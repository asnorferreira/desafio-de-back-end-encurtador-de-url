package com.asnorferreira.desafio.demo.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta da URL encurtada")
public record UrlStatisticsResponse(
        String shortUrl,
        String originalUrl,
        long totalAccesses,
        double averageAccessesPerDay
) {
}