package com.example.progettoschedly.data.service.impl;

import com.example.progettoschedly.data.entity.Esp;
import com.example.progettoschedly.data.entity.Piano;
import com.example.progettoschedly.data.repository.EspRepository;
import com.example.progettoschedly.data.repository.PianoRepository;
import com.example.progettoschedly.dto.EspDTO;
import com.example.progettoschedly.data.service.EspService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EspServiceImpl implements EspService {

    private final EspRepository espRepository;
    private final PianoRepository pianoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<EspDTO> getByPiano(Long pianoId) {
        return espRepository.findByPianoId(pianoId).stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public EspDTO createForPiano(Long pianoId, EspDTO dto) {
        Piano piano = pianoRepository.findById(pianoId)
                .orElseThrow(() -> new IllegalArgumentException("Piano non trovato"));

        Esp entity = new Esp();
        entity.setFormato(dto.getFormato());
        entity.setDataEsportazione(dto.getDataEsportazione() != null
                ? dto.getDataEsportazione()
                : LocalDateTime.now());
        entity.setPiano(piano);

        Esp saved = espRepository.save(entity);
        return toDto(saved);
    }

    @Override
    public boolean delete(Long id) {
        if (!espRepository.existsById(id)) {
            return false;
        }
        espRepository.deleteById(id);
        return true;
    }

    private EspDTO toDto(Esp entity) {
        EspDTO dto = new EspDTO();
        dto.setId(entity.getId());
        dto.setFormato(entity.getFormato());
        dto.setDataEsportazione(entity.getDataEsportazione());
        dto.setPianoId(entity.getPiano() != null ? entity.getPiano().getId() : null);
        return dto;
    }
}

