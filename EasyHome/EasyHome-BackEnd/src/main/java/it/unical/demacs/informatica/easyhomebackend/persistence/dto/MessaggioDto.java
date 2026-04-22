package it.unical.demacs.informatica.easyhomebackend.persistence.dto;

import it.unical.demacs.informatica.easyhomebackend.model.Utente;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessaggioDto {
    private int id;
    private String oggetto;
    private String descrizione;
    private String acquirente;
    private int idImmobile;
    private String nomeImmobile;
    private String email;
    private long telefono;
}
