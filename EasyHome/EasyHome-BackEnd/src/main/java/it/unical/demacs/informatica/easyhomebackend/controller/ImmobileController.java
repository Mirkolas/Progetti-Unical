package it.unical.demacs.informatica.easyhomebackend.controller;

import it.unical.demacs.informatica.easyhomebackend.model.Immobile;
import it.unical.demacs.informatica.easyhomebackend.model.ImmobileMinimal;
import it.unical.demacs.informatica.easyhomebackend.model.UserRole;
import it.unical.demacs.informatica.easyhomebackend.model.Utente;
import it.unical.demacs.informatica.easyhomebackend.persistence.dto.ImmobileDto;
import it.unical.demacs.informatica.easyhomebackend.persistence.dto.MarkerDTO;
import it.unical.demacs.informatica.easyhomebackend.service.IImmobileService;
import it.unical.demacs.informatica.easyhomebackend.service.ImmobileService;
import it.unical.demacs.informatica.easyhomebackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ImmobileController {

    private final IImmobileService immobileService= new ImmobileService();
    private final UserService userService;

    public ImmobileController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/auth/immobili/createImmobile", method = RequestMethod.POST)
    public ResponseEntity<Void> createImmobile(
            @ModelAttribute ImmobileDto immobileDto) throws Exception {
        Immobile nuovoImmobile = componiImmobile(immobileDto);


        this.immobileService.createImmobile(nuovoImmobile,immobileDto.getFoto(),immobileDto.getUser());
        return ResponseEntity.ok().build();
    }

    private Immobile componiImmobile(@ModelAttribute ImmobileDto immobileDto) {
        Immobile nuovoImmobile = new Immobile();
        nuovoImmobile.setId(null);
        nuovoImmobile.setNome(immobileDto.getNome());
        nuovoImmobile.setTipo(immobileDto.getTipo());
        nuovoImmobile.setDescrizione(immobileDto.getDescrizione());
        nuovoImmobile.setCategoria(immobileDto.getCategoria());
        nuovoImmobile.setPrezzo(immobileDto.getPrezzo());
        nuovoImmobile.setMq(immobileDto.getMq());
        nuovoImmobile.setCamere(immobileDto.getCamere());
        nuovoImmobile.setBagni(immobileDto.getBagni());
        nuovoImmobile.setAnno(immobileDto.getAnno());
        nuovoImmobile.setData(immobileDto.getData());
        nuovoImmobile.setProvincia(immobileDto.getProvincia());
        nuovoImmobile.setLatitudine(immobileDto.getLatitudine());
        nuovoImmobile.setLongitudine(immobileDto.getLongitudine());
        nuovoImmobile.setFotoPaths(new ArrayList<>());
        nuovoImmobile.setPrezzo_scontato(null);
        return nuovoImmobile;
    }


    @RequestMapping(value = "/open/immobili", method = RequestMethod.GET)
    public ResponseEntity<List<ImmobileMinimal>> getImmobiliMinimal(
            @RequestParam(value = "tipo", required = false) String tipo,
            @RequestParam(value = "categoria", required = false) String categoria,
            @RequestParam(value = "provincia", required = false) String provincia){

        // Utilizza i parametri per filtrare i risultati
        List<ImmobileMinimal> immobiliMinimal = this.immobileService.getImmobiliFilteredMinimal(tipo,categoria,provincia);
        return ResponseEntity.ok(immobiliMinimal);
    }

    @RequestMapping(value = "/auth/immobili/{username}", method = RequestMethod.GET)
    public ResponseEntity<List<ImmobileMinimal>> getImmobiliMinimalByUsername(
            @PathVariable("username") String username) {

        // Recupera l'utente
        Optional<Utente> utenteOptional = userService.getUser(username);

        if (utenteOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Se non esiste, restituisci 404
        }

        Utente utente = utenteOptional.get();
        if (utente.getRole() == UserRole.ROLE_ADMIN) {
            List<Immobile> tuttiGliImmobili = immobileService.getAllImmobili();

            List<ImmobileMinimal> immobiliMinimal = tuttiGliImmobili.stream()
                    .map(immobile -> new  ImmobileMinimal(
                                    immobile.getId(),
                                    immobile.getNome(),
                                    immobile.getPrezzo(),
                                    immobile.getTipo(),
                                    immobile.getCategoria(),
                                    immobile.getMq(),
                            immobile.getFotoPaths() != null && !immobile.getFotoPaths().isEmpty()
                                    ? immobile.getFotoPaths().getFirst()
                                    : "default.jpg",

                            immobile.getPrezzo_scontato()
                            ))
                            .toList();
            return ResponseEntity.ok(immobiliMinimal);
        }

        // Se non è admin, restituisci solo gli immobili dell'utente
        List<ImmobileMinimal> immobiliMinimal = immobileService.getImmobiliMinimalByUsername(username);
        return ResponseEntity.ok(immobiliMinimal);
    }


    @GetMapping("/open/markers")
    public ResponseEntity<List<MarkerDTO>> getAllMarkers() {
        List<MarkerDTO> markers;
        markers = immobileService.getAllMarkers();

        return ResponseEntity.ok(markers);
    }

    @RequestMapping(value = "/auth/immobili/deleteImmobile/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteImmobile(@PathVariable("id") int id) {
        try {

            immobileService.deleteImmobile(id);
            return ResponseEntity.noContent().build(); // Successo (204 No Content)
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Errore (500 Internal Server Error)
        }
    }

    @RequestMapping(value = "/auth/immobili/updateImmobile/{id}", method = RequestMethod.POST)
    public ResponseEntity<Void> updateImmobile(@PathVariable("id") int id, @ModelAttribute ImmobileDto immobileDto) throws Exception {
        Immobile nuovoImmobile = componiImmobile(immobileDto);
        nuovoImmobile.setId(id);
        this.immobileService.updateImmobile(nuovoImmobile,immobileDto.getFoto());
        return ResponseEntity.ok().build();
    }


    @GetMapping("/auth/dettaglio/{id}")
    public ResponseEntity<Immobile> getImmobileById(@PathVariable int id) {

        Immobile immobile = immobileService.getImmobileById(id);
        if (immobile != null) {
            return ResponseEntity.ok(immobile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
