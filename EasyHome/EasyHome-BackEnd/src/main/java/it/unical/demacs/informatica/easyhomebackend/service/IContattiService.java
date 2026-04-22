package it.unical.demacs.informatica.easyhomebackend.service;

import it.unical.demacs.informatica.easyhomebackend.model.Contatti;
import it.unical.demacs.informatica.easyhomebackend.model.Immobile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IContattiService {

    Contatti CreaRichiesta(Contatti contatti);

}
