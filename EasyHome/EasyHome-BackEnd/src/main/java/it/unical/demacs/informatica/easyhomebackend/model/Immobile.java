package it.unical.demacs.informatica.easyhomebackend.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
public class Immobile {
    private Integer id;
    private String nome;
    private String tipo;
    private String descrizione;
    private String categoria;
    private int prezzo;
    private int mq;
    private int camere;
    private int bagni;
    private int anno;
    private String data;
    private String provincia;
    private double latitudine;
    private double longitudine;
    private List<String> fotoPaths;
    private List<byte[]> foto;
    private Utente utente;
    private Integer prezzo_scontato;

    protected List<Messaggio> messaggi;

    // Costruttori, getter e setter
    public Immobile() {
    }

    public Immobile(int id, String nome, String tipo, String descrizione, String categoria, int prezzo, int mq, int camere, int bagni, int anno, String data, String provincia, double latitudine, double longitudine, List<String> fotoPaths, Integer prezzo_scontato) {
        this.nome = nome;
        this.id = id;
        this.fotoPaths = fotoPaths;  // Assegna il file come byte array
        this.descrizione = descrizione;
        this.categoria=categoria;
        this.tipo = tipo;
        this.prezzo = prezzo;
        this.mq = mq;
        this.camere = camere;
        this.bagni = bagni;
        this.anno = anno;
        this.data = data;
        this.provincia = provincia;
        this.latitudine = latitudine;
        this.longitudine = longitudine;
        this.prezzo_scontato = prezzo_scontato;
    }
}

