package it.unical.demacs.informatica.easyhomebackend.service;

import it.unical.demacs.informatica.easyhomebackend.model.Immobile;
import it.unical.demacs.informatica.easyhomebackend.model.ImmobileMinimal;
import it.unical.demacs.informatica.easyhomebackend.persistence.dto.MarkerDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface IImmobileService {

        Immobile createImmobile(Immobile nuovoImmobile, List<MultipartFile> foto, String user) throws Exception;

        void updateImmobile(Immobile immobile, List<MultipartFile> foto) throws Exception;


        Optional<Immobile> getImmobile(int id);
        List<Immobile> getAllImmobili();
        List<ImmobileMinimal> getImmobiliFilteredMinimal(String tipo, String categoria, String provincia);

        List<MarkerDTO> getAllMarkers();

        Immobile getImmobileById(int id);

        void deleteImmobile(int id) throws Exception;

        List<ImmobileMinimal> getImmobiliMinimalByUsername(String username);

}


