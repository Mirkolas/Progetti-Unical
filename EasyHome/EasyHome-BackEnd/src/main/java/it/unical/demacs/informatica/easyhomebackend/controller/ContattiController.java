package it.unical.demacs.informatica.easyhomebackend.controller;

import it.unical.demacs.informatica.easyhomebackend.model.Contatti;
import it.unical.demacs.informatica.easyhomebackend.service.IContattiService;
import it.unical.demacs.informatica.easyhomebackend.service.ContattiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ContattiController {

    private final ContattiService contattiService = new ContattiService();

    public ContattiController() { }

    @RequestMapping(value = "/auth/CreaRichiesta", method = RequestMethod.POST)
    public ResponseEntity<Void> CreaRichiesta(
            @RequestBody Contatti contatti) {

        // Chiamata al service per creare la richiesta di contatto
        this.contattiService.CreaRichiesta(contatti);
        return ResponseEntity.ok().build();
    }



}
