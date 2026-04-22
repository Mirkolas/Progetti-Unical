package it.unical.demacs.informatica.easyhomebackend.persistence.dao;

import it.unical.demacs.informatica.easyhomebackend.model.Recensione;
import it.unical.demacs.informatica.easyhomebackend.model.Utente;

import java.util.List;

public interface RecensioneDao {



    void save(Recensione recensione);

    void deleteByImmobileId(int idImmobile);

    List<Recensione> findByImmobileId(int idImmobile);

    void delete(int id);
}
