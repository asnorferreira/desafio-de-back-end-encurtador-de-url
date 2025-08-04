package com.asnorferreira.desafio.demo.domain.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AccessRecord {
    private LocalDateTime accessTime;

    public AccessRecord(LocalDateTime accessTime){
        this.accessTime = accessTime;
    }
}
