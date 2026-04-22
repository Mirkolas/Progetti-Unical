package com.example.progettoschedly.data.service.impl;

import com.example.progettoschedly.data.entity.Utente;
import com.example.progettoschedly.data.entity.Vincoli;
import com.example.progettoschedly.data.repository.UtenteRepository;
import com.example.progettoschedly.data.repository.VincoliRepository;
import com.example.progettoschedly.dto.VincoliDTO;
import com.example.progettoschedly.data.service.VincoliService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class VincoliServiceImpl implements VincoliService {

    private final VincoliRepository vincoliRepository;
    private final UtenteRepository utenteRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<VincoliDTO> getByUser(Long userId) {
        return Optional.ofNullable(vincoliRepository.findByUtenteId(userId))
                .map(this::toDto);
    }

    @Override
    public VincoliDTO upsertForUser(Long userId, VincoliDTO dto) {
        Utente utente = utenteRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        Vincoli vincoli = Optional.ofNullable(vincoliRepository.findByUtenteId(userId))
                .orElseGet(Vincoli::new);

        applyFields(vincoli, dto);
        vincoli.setUtente(utente);

        Vincoli saved = vincoliRepository.save(vincoli);
        return toDto(saved);
    }

    private void applyFields(Vincoli entity, VincoliDTO dto) {
        entity.setFasceOrarieDisponibili(dto.getFasceOrarieDisponibili());
        entity.setGiorniPreferiti(dto.getGiorniPreferiti());
        entity.setLimiteCaricoGiornaliero(dto.getLimiteCaricoGiornaliero());
        entity.setImpostazioniNotifiche(dto.getImpostazioniNotifiche());
    }

    private VincoliDTO toDto(Vincoli entity) {
        VincoliDTO dto = new VincoliDTO();
        dto.setId(entity.getId());
        dto.setFasceOrarieDisponibili(entity.getFasceOrarieDisponibili());
        dto.setGiorniPreferiti(entity.getGiorniPreferiti());
        dto.setLimiteCaricoGiornaliero(entity.getLimiteCaricoGiornaliero());
        dto.setImpostazioniNotifiche(entity.getImpostazioniNotifiche());
        dto.setUtenteId(entity.getUtente() != null ? entity.getUtente().getId() : null);
        return dto;
    }
}

