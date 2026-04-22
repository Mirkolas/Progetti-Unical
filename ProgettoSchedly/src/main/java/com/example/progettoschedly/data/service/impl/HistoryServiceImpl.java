package com.example.progettoschedly.data.service.impl;

import com.example.progettoschedly.data.entity.History;
import com.example.progettoschedly.data.entity.Piano;
import com.example.progettoschedly.data.repository.HistoryRepository;
import com.example.progettoschedly.data.repository.PianoRepository;
import com.example.progettoschedly.dto.HistoryDTO;
import com.example.progettoschedly.data.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;
    private final PianoRepository pianoRepository;


    @Override
    @Transactional(readOnly = true)
    public List<HistoryDTO> getByPiano(Long pianoId) {
        return historyRepository.findByPianoId(pianoId).stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoryDTO> getByUser(Long userId) {
        return historyRepository.findByUtenteId(userId).stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public HistoryDTO createForPiano(Long pianoId, HistoryDTO dto) {
        Piano piano = pianoRepository.findById(pianoId)
                .orElseThrow(() -> new IllegalArgumentException("Piano non trovato"));

        History entity = new History();
        entity.setDataArchiviazione(dto.getDataArchiviazione() != null
                ? dto.getDataArchiviazione()
                : LocalDateTime.now());
        entity.setPiano(piano);

        History saved = historyRepository.save(entity);
        return toDto(saved);
    }

    @Override
    public boolean delete(Long id) {
        if (!historyRepository.existsById(id)) {
            return false;
        }
        historyRepository.deleteById(id);
        return true;
    }

    private HistoryDTO toDto(History entity) {
        HistoryDTO dto = new HistoryDTO();
        dto.setId(entity.getId());
        dto.setDataArchiviazione(entity.getDataArchiviazione());
        dto.setPianoId(entity.getPiano() != null ? entity.getPiano().getId() : null);
        return dto;
    }
}

