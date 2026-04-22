package com.example.progettoschedly.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "esportazioni")
public class Esp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String formato;
    private LocalDateTime dataEsportazione;


    @ManyToOne
    @JoinColumn(name = "piano_id")
    private Piano piano;
}