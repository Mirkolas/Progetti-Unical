package com.example.progettoschedly.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EspDTO {
    private Long id;
    private String formato;
    private LocalDateTime dataEsportazione;
    private Long pianoId;
}

