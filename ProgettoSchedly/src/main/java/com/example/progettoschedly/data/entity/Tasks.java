package com.example.progettoschedly.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "attivita")
public class Tasks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titolo;
    private Integer durata;
    private LocalDate giorno;
    private String fasciaOraria;
    private LocalDate scadenza;
    private String priorita;

    @Column(length = 2000)
    private String note;

    private String stato;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "piano_id")
    private Piano piano;

    @OneToMany(mappedBy = "attivita", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notifica> notifiche = new ArrayList<>();
}
