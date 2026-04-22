package it.unical.demacs.informatica.easyhomebackend.service;

import it.unical.demacs.informatica.easyhomebackend.model.Messaggio;

public interface IMessaggioService {
    void createMessaggio(Messaggio messaggio, String acquirente,int immobileId);

    void deleteMessaggio(int idMessaggio);
}
