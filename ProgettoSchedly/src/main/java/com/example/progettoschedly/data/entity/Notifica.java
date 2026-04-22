package com.example.progettoschedly.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;
@Data
@Entity
@Table(name = "notifiche",
        uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_notifica_attivita_tipo",
                columnNames = {"attivita_id", "tipo"}
        )
})
public class Notifica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo;
    private LocalTime orarioNotifica;



    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "attivita_id", nullable = false)
    private Tasks attivita;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;
}
