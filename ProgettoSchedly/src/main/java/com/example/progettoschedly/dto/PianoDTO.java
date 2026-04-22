package com.example.progettoschedly.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PianoDTO {
    private Long id;
    private String periodo;
    private LocalDateTime dataCreazione;
    private LocalDateTime dataRigenerazione;
    private Long utenteId;
    private List<Long> attivitaIds;
    private List<Long> storicoIds;
    private List<Long> esportazioniIds;
}

