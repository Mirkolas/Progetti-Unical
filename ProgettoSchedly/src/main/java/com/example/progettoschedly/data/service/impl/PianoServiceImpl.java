package com.example.progettoschedly.data.service.impl;

import com.example.progettoschedly.data.entity.*;
import com.example.progettoschedly.data.repository.*;
import com.example.progettoschedly.data.service.PianoService;
import com.example.progettoschedly.dto.PianoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PianoServiceImpl implements PianoService {

    private final PianoRepository pianoRepository;
    private final UtenteRepository utenteRepository;
    private final HistoryRepository historyRepository;
    private final EspRepository espRepository;
    private final TasksRepository taskRepository;
    private final NotificaRepository notificaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PianoDTO> getAll() {
        return pianoRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PianoDTO> getById(Long id) {
        return pianoRepository.findById(id).map(this::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PianoDTO> getByUser(Long userId) {
        return pianoRepository.findAllByUtenteIdOrderByDataCreazioneDesc(userId)
                .stream()
                .findFirst()
                .map(this::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PianoDTO> getAllByUser(Long userId) {
        return pianoRepository.findAllByUtenteIdOrderByDataCreazioneDesc(userId).stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public PianoDTO createForUser(Long userId, PianoDTO dto) {
        Utente utente = utenteRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        Piano piano = new Piano();
        applyFields(piano, dto);
        piano.setUtente(utente);

        if (piano.getDataCreazione() == null) piano.setDataCreazione(LocalDateTime.now());
        if (piano.getDataRigenerazione() == null) piano.setDataRigenerazione(LocalDateTime.now());

        Piano saved = pianoRepository.save(piano);
        return toDto(saved);
    }

    @Override
    public PianoDTO saveForUser(Long userId, PianoDTO dto) {
        return createForUser(userId, dto);
    }

    @Override
    public boolean delete(Long id) {
        if (!pianoRepository.existsById(id)) return false;
        pianoRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional
    public void deleteAllByUser(Long userId) {
        notificaRepository.deleteAllByUtenteId(userId);
        taskRepository.deleteAllByUtenteId(userId);
        historyRepository.deleteAllByUtenteId(userId);
        espRepository.deleteAllByUtenteId(userId);
        pianoRepository.deleteAllByUtenteId(userId);
    }

    private void applyFields(Piano entity, PianoDTO dto) {
        entity.setPeriodo(dto.getPeriodo());
        entity.setDataCreazione(dto.getDataCreazione());
        entity.setDataRigenerazione(dto.getDataRigenerazione());
    }

    private PianoDTO toDto(Piano entity) {
        PianoDTO dto = new PianoDTO();
        dto.setId(entity.getId());
        dto.setPeriodo(entity.getPeriodo());
        dto.setDataCreazione(entity.getDataCreazione());
        dto.setDataRigenerazione(entity.getDataRigenerazione());
        dto.setUtenteId(entity.getUtente() != null ? entity.getUtente().getId() : null);

        List<Long> attivitaIds = entity.getAttivita() == null
                ? Collections.emptyList()
                : entity.getAttivita().stream()
                .map(Tasks::getId)
                .filter(Objects::nonNull)
                .toList();
        dto.setAttivitaIds(attivitaIds);

        List<Long> storicoIds = entity.getStorico() == null
                ? Collections.emptyList()
                : entity.getStorico().stream()
                .map(History::getId)
                .filter(Objects::nonNull)
                .toList();
        dto.setStoricoIds(storicoIds);

        List<Long> esportazioniIds = entity.getEsportazioni() == null
                ? Collections.emptyList()
                : entity.getEsportazioni().stream()
                .map(Esp::getId)
                .filter(Objects::nonNull)
                .toList();
        dto.setEsportazioniIds(esportazioniIds);

        return dto;
    }
}
