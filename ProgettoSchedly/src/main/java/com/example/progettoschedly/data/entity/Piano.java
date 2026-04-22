package com.example.progettoschedly.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "piani")
public class Piano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String periodo;
    private LocalDateTime dataCreazione;
    private LocalDateTime dataRigenerazione;

    @ManyToOne
    @JoinColumn(name = "utente_id", nullable = false)
    @JsonIgnore
    private Utente utente;

    @OneToMany(mappedBy = "piano", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tasks> attivita = new ArrayList<>();

    @OneToMany(mappedBy = "piano", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<History> storico = new ArrayList<>();

    @OneToMany(mappedBy = "piano", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Esp> esportazioni = new ArrayList<>();
}
