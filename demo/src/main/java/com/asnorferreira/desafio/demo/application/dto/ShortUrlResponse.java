package com.asnorferreira.desafio.demo.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta da URL encurtada")
public record ShortUrlResponse(
        String shortUrl,
        String originalUrl,
        String createdAt
) {
}