package com.asnorferreira.desafio.demo.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Requisição para encurtar uma URL")
public record ShortUrlRequest(
        @NotBlank(message = "URL cannot be empty")
        @Schema(description = "URL original a ser encurtada", example = "https://example.com")
        @Pattern(regexp = "^https?://.*", message = "Invalid URL format")
        String originalUrl
) {
}
