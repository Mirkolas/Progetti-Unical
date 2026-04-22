package it.unical.demacs.informatica.easyhomebackend.service;


import it.unical.demacs.informatica.easyhomebackend.model.Recensione;
import it.unical.demacs.informatica.easyhomebackend.persistence.dao.RecensioneDao;
import it.unical.demacs.informatica.easyhomebackend.persistence.dto.MessaggioDto;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface IRecensioneService {
    void creaRecensione(Recensione recensione);
    
    Optional<Recensione> getImmobile(String id);
}
