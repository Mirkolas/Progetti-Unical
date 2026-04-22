package it.unical.demacs.informatica.easyhomebackend.model;

import it.unical.demacs.informatica.easyhomebackend.persistence.dao.ImmobileDao;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Messaggio {
    private Integer id;
    private String oggetto;
    private String descrizione;
    protected Utente acquirente;
    private String email;
    private long telefono;
    protected Immobile immobile;

    public Messaggio(String oggetto, String descrizione) {
        this.oggetto = oggetto;
        this.descrizione = descrizione;
    }
}
