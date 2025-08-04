package com.asnorferreira.desafio.demo.domain.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "short_urls")
public class ShortUrl {
    @Id
    private String id; 
    private String originalUrl;
    private String shortCode;
    private LocalDateTime createdAt;
    private List<AccessRecord> accessRecords;

    public ShortUrl(String originalUrl, String shortCode) {
        this.originalUrl = originalUrl;
        this.shortCode = shortCode;
        this.createdAt = LocalDateTime.now();
        this.accessRecords = new ArrayList<>();
    }

    public void recordAccess() {
        this.accessRecords.add(new AccessRecord(LocalDateTime.now()));
    }
}
