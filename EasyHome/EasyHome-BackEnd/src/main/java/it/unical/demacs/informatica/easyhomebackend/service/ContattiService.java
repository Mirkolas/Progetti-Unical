package it.unical.demacs.informatica.easyhomebackend.service;

import it.unical.demacs.informatica.easyhomebackend.model.Contatti;
import it.unical.demacs.informatica.easyhomebackend.persistence.DBManager;
import it.unical.demacs.informatica.easyhomebackend.persistence.dao.ContattiDao;
import org.springframework.stereotype.Service;

@Service // Indica che è un servizio gestito da Spring
public class ContattiService {

    private final ContattiDao contattiDao = DBManager.getInstance().getContattiDao();

    public ContattiService() {
    }

    public Contatti CreaRichiesta(Contatti contatti) {
        // Validazione dei campi obbligatori
        if (contatti.getNome() == null || contatti.getNome().isEmpty() || contatti.getEmail() == null || contatti.getEmail().isEmpty() ||
                contatti.getCognome() == null || contatti.getCognome().isEmpty() ||
                contatti.getDomanda() == null || contatti.getDomanda().isEmpty()) {
            throw new IllegalArgumentException("I dati del contatto non sono validi");
        }

        try {
            // Salvataggio della richiesta nel database
            contattiDao.save(contatti);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Errore durante il salvataggio del contatto", e);
        }

        // Restituisco la richiesta di contatto con l'ID generato
        return contatti;
    }
}
