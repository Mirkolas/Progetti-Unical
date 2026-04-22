package it.unical.demacs.informatica.easyhomebackend.service;

import it.unical.demacs.informatica.easyhomebackend.model.Messaggio;
import it.unical.demacs.informatica.easyhomebackend.persistence.DBManager;
import it.unical.demacs.informatica.easyhomebackend.persistence.dao.ImmobileDao;
import it.unical.demacs.informatica.easyhomebackend.persistence.dao.MessaggioDao;

public class MessaggioService implements IMessaggioService {
    private final MessaggioDao messaggioDao= DBManager.getInstance().getMessaggioDao();


    public MessaggioService() {
    }

    @Override
    public void createMessaggio(Messaggio messaggio, String acquirente,int immobileId) {
        if (messaggio.getOggetto() == null) {
            throw new IllegalArgumentException("I dati del messaggio non sono validi");
        }
        try {
            // Salvataggio dell'immobile
            this.messaggioDao.save(messaggio, acquirente,immobileId);
        } catch (Exception e) {
            // Gestione delle eccezioni
            e.printStackTrace();
            throw new RuntimeException("Errore durante il salvataggio dell'immobile", e);
        }
    }

    @Override
    public void deleteMessaggio(int idMessaggio) {
        try {
            this.messaggioDao.delete(idMessaggio);
        } catch (Exception e) {
            // Gestione delle eccezioni
            e.printStackTrace();
            throw new RuntimeException("Errore durante il salvataggio dell'immobile", e);
        }
    }
}
