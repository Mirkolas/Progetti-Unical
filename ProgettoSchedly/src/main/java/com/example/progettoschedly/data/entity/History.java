package com.example.progettoschedly.data.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "storico_attivita")
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataArchiviazione;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "piano_id")
    private Piano piano;
}