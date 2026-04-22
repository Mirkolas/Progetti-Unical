package com.example.progettoschedly.dto;

import lombok.Data;

import java.util.List;

@Data
public class UtenteDTO {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Long vincoliId;
    private Long pianoId;
    private List<Long> attivitaIds;
    private List<Long> notificheIds;
}

