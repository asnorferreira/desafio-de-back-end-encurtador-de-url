package com.asnorferreira.desafio.demo.infrastructure.repository;

import com.asnorferreira.desafio.demo.domain.model.ShortUrl;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ShortUrlRepository extends MongoRepository<ShortUrl, String> {
    Optional<ShortUrl> findByShortCode(String shortCode);
}