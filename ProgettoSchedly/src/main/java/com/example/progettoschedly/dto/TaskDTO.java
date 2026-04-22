package com.example.progettoschedly.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskDTO {
    private Long id;
    private String titolo;
    private Integer durata;
    @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE)
    private LocalDate giorno;
    private String fasciaOraria;
    @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE)
    private LocalDate scadenza;
    private String priorita;
    private String note;
    private String stato;
    private Long utenteId;
    private Long pianoId;
}

