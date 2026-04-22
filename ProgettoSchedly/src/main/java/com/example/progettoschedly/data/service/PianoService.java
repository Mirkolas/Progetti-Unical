package com.example.progettoschedly.data.service;

import com.example.progettoschedly.dto.PianoDTO;

import java.util.List;
import java.util.Optional;

public interface PianoService {

    List<PianoDTO> getAll();

    Optional<PianoDTO> getById(Long id);

    Optional<PianoDTO> getByUser(Long userId);

    List<PianoDTO> getAllByUser(Long userId);

    PianoDTO createForUser(Long userId, PianoDTO dto);

    //
    PianoDTO saveForUser(Long userId, PianoDTO dto);

    boolean delete(Long id);

    void deleteAllByUser(Long userId);
}
