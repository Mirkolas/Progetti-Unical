package com.example.progettoschedly.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class NotificaDTO {
    private Long id;
    private String tipo;
    private LocalTime orarioNotifica;
    private Long attivitaId;
    private Long utenteId;
}

