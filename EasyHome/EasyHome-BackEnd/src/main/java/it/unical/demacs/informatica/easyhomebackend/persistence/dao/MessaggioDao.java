package it.unical.demacs.informatica.easyhomebackend.persistence.dao;

import it.unical.demacs.informatica.easyhomebackend.model.Messaggio;
import it.unical.demacs.informatica.easyhomebackend.persistence.dto.MessaggioDto;

import java.util.List;

public interface MessaggioDao {
    void save(Messaggio messaggio, String acquirente,int immobileId);
    List<MessaggioDto> findByImmobileId(int idImmobile);

    void delete(int idMessaggio);

    void deleteAllByImmobileId(int idImmobile);
}
