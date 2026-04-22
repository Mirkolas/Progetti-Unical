
package it.unical.demacs.informatica.easyhomebackend.controller;



import it.unical.demacs.informatica.easyhomebackend.model.*;
import it.unical.demacs.informatica.easyhomebackend.persistence.dto.MessaggioDto;
import it.unical.demacs.informatica.easyhomebackend.persistence.dto.UserRoleDto;
import it.unical.demacs.informatica.easyhomebackend.service.IUserService;
import it.unical.demacs.informatica.easyhomebackend.service.ImmobileService;
import it.unical.demacs.informatica.easyhomebackend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UtenteController {

    private final IUserService userService;
    private final ImmobileService immobileService;

    public UtenteController(IUserService userService, ImmobileService immobileService) {
        this.userService = userService;
        this.immobileService = immobileService;
    }

    @RequestMapping(value = "/open/createUser" , method = RequestMethod.POST)
    public ResponseEntity<Void> createUser(@RequestBody Utente utente) {
        utente.setRole(UserRole.ROLE_USER);
        this.userService.createUser(utente);

        return  ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/admin/users", method = RequestMethod.GET)
    public List<UserRoleDto> getAllUsers() {
        // Usa il nuovo metodo
        return userService.getAllUsernamesAndRoles();  // Restituisci la lista con username e ruolo
    }


    @GetMapping("/auth/{username}/immobili")
    public ResponseEntity<List<Immobile>> getImmobiliUtente(@PathVariable String username) {
        Optional<Utente> utenteOptional = userService.getUser(username);

        // Verifica se l'utente è presente nel Optional
        if (utenteOptional.isPresent()) {
            Utente utente = utenteOptional.get();


            return ResponseEntity.ok(utente.getImmobili());  // Ora puoi chiamare getImmobili
        } else {
            return ResponseEntity.notFound().build();  // Rispondi con un 404 se l'utente non è trovato
        }
    }

    @GetMapping("/auth/{username}/messaggi")
    public ResponseEntity<List<MessaggioDto>> getMessaggiUtente(@PathVariable String username) {
        Optional<Utente> utenteOptional = userService.getUser(username);
        if (utenteOptional.isPresent()) {
            Utente utente = utenteOptional.get();  // Estrai l'utente dall'Optional
            return ResponseEntity.ok(utente.getMessaggi());
        } else {
            return ResponseEntity.notFound().build();  // Rispondi con un 404 se l'utente non è trovato
        }
    }

    @GetMapping("/auth/{username}/recensioni")
    public ResponseEntity<List<Recensione>> getRecensioniUtente(@PathVariable String username) {
        Optional<Utente> utenteOptional = userService.getUser(username);
        if (utenteOptional.isPresent()) {
            Utente utente = utenteOptional.get();  // Estrai l'utente dall'Optional

            return ResponseEntity.ok(utente.getRecensioni());
        } else {
            return ResponseEntity.notFound().build();  // Rispondi con un 404 se l'utente non è trovato
        }
    }

}