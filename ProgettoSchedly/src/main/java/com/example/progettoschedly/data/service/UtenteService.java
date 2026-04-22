package com.example.progettoschedly.data.service;

import com.example.progettoschedly.dto.UtenteDTO;

import java.util.List;
import java.util.Optional;

public interface UtenteService {
    List<UtenteDTO> getAll();
    Optional<UtenteDTO> getById(Long id);
    Optional<UtenteDTO> getByUsername(String username);
    UtenteDTO create(UtenteDTO dto);
    Optional<UtenteDTO> update(Long id, UtenteDTO dto);
    boolean delete(Long id);
}
