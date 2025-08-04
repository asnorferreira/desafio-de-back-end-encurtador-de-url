package com.asnorferreira.desafio.demo.application.controller;

import com.asnorferreira.desafio.demo.application.dto.ShortUrlRequest;
import com.asnorferreira.desafio.demo.application.dto.ShortUrlResponse;
import com.asnorferreira.desafio.demo.application.dto.UrlStatisticsResponse;
import com.asnorferreira.desafio.demo.service.ShortUrlService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/v1/urls")
@Tag(name = "Encurtador de URL", description = "Endpoints para encurtar, redirecionar e obter estatísticas de URLs")
public class ShortUrlController {
    private final ShortUrlService shortUrlService;

    public ShortUrlController(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }

    @Operation(summary = "Encurtar uma URL original", responses = {
        @ApiResponse(responseCode = "201", description = "URL encurtada com sucesso",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ShortUrlResponse.class))),
        @ApiResponse(responseCode = "400", description = "URL inválida",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = String.class)))
    })
    @PostMapping
    public ResponseEntity<ShortUrlResponse> createShortUrl(
            @Valid @RequestBody ShortUrlRequest request) {
        ShortUrlResponse response = shortUrlService.createShortUrl(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Redirecionar a partir de uma URL encurtada", responses = {
            @ApiResponse(responseCode = "302", description = "Redirecionamento para URL original",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "URL encurtada não encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectToOriginalUrl(
            @Parameter(description = "Código encurtado") @PathVariable String shortCode) {
        String originalUrl = shortUrlService.getOriginalUrl(shortCode);
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", originalUrl)
                .build();
    }

    @Operation(summary = "Obter estatísticas de uma URL encurtada", responses = {
        @ApiResponse(responseCode = "200", description = "Estatísticas retornadas com sucesso",
                content = @io.swagger.v3.oas.annotations.media.Content(
                        mediaType = "application/json",
                        schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = UrlStatisticsResponse.class))),
        @ApiResponse(responseCode = "404", description = "URL encurtada não encontrada")
    })
    @GetMapping("/{shortCode}/stats")
    public ResponseEntity<UrlStatisticsResponse> getStatistics(
            @Parameter(description = "Código encurtado") @PathVariable String shortCode) {
        UrlStatisticsResponse response = shortUrlService.getStatistics(shortCode);
        return ResponseEntity.ok(response);
    }
}
