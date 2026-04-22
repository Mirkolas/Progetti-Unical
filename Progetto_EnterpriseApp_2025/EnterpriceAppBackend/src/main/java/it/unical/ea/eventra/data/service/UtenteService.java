package it.unical.ea.eventra.data.service;

import it.unical.ea.eventra.data.entity.Utente;
import it.unical.ea.eventra.dto.UtenteDTO;
import it.unical.ea.eventra.dto.RequestAuthentication;

import java.util.List;
import java.util.Map;

public interface UtenteService {

    void save (Utente utente);

    void RegistrazioneUtente(UtenteDTO utenteDTO) throws Exception;

    Utente AutenticazioneUtente(RequestAuthentication utenteLoginDTO) throws Exception;

    List<Utente> getAllUtenti();

    boolean aggiornaProfilo(Long userId, String emailFromToken, String nome, String cognome, String numeroTelefono);

    UtenteDTO getById(Long id);

    void deleteUtente(Long id);

    Utente getOrCreateUser(String email, Map<String, Object> attributes);

    boolean aggiornaPassword(String usernameFromToken, Long userId, String oldPassword, String newPassword);
}
