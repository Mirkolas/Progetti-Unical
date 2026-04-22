package com.example.progettoschedly.dto;

import lombok.Data;

@Data
public class VincoliDTO {
    private Long id;
    private String fasceOrarieDisponibili;
    private String giorniPreferiti;
    private Integer limiteCaricoGiornaliero;
    private String impostazioniNotifiche;
    private Long utenteId;
}

