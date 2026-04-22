package it.unical.demacs.informatica.easyhomebackend.model;

import lombok.*;

@Getter
@Setter
@ToString
public class Contatti {
    private int id;
    private String nome;
    private String email;
    private String cognome;
    private String tipo;
    private String domanda;


    public Contatti(int id, String nome, String cognome,String email, String tipo, String domanda) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.tipo = tipo;
        this.domanda = domanda;
    }
}
