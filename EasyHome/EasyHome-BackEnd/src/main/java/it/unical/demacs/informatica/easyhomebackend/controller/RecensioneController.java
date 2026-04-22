package it.unical.demacs.informatica.easyhomebackend.controller;

import it.unical.demacs.informatica.easyhomebackend.model.Recensione;
import it.unical.demacs.informatica.easyhomebackend.persistence.dto.MessaggioDto;
import it.unical.demacs.informatica.easyhomebackend.service.RecensioneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RecensioneController {

    private final RecensioneService recensioneService;

    public RecensioneController(RecensioneService recensioneService) {
        this.recensioneService = recensioneService;
    }

    // Endpoint per creare una recensione
    @PostMapping("/auth/recensioni/createRecensione")
    public ResponseEntity<Void> createRecensione(@ModelAttribute Recensione recensione) {
        this.recensioneService.creaRecensione(recensione);
        return ResponseEntity.ok().build();
    }



}
