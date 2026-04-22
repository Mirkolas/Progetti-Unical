package it.unical.demacs.informatica.easyhomebackend.service;

import it.unical.demacs.informatica.easyhomebackend.model.Recensione;
import it.unical.demacs.informatica.easyhomebackend.persistence.DBManager;
import it.unical.demacs.informatica.easyhomebackend.persistence.dao.RecensioneDao;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service // Indica che è un service gestito da Spring
public class RecensioneService {

    private final RecensioneDao recensioneDao = DBManager.getInstance().getRecensioneDao();

    public RecensioneService() {
    }

    public void creaRecensione(Recensione recensione) {
        // Validazione dei campi obbligatori
        if (recensione.getRating() < 1 || recensione.getRating() > 5 || recensione.getDescrizione() == null || recensione.getDescrizione().isEmpty()) {
            throw new IllegalArgumentException("I dati della recensione non sono validi");
        }

        try {
            // Salvataggio della recensione nel database
            recensioneDao.save(recensione);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Errore durante il salvataggio della recensione", e);
        }
    }

}
