package com.example.progettoschedly.data.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "vincoli_preferenze")
public class Vincoli {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fasceOrarieDisponibili;
    private String giorniPreferiti;
    private Integer limiteCaricoGiornaliero;
    private String impostazioniNotifiche;

    @OneToOne
    @JoinColumn(name = "utente_id", nullable = false, unique = true)
    private Utente utente;

}