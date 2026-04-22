package com.example.progettoschedly.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HistoryDTO {
    private Long id;
    private LocalDateTime dataArchiviazione;
    private Long pianoId;
}

