package com.example.progettoschedly.data.service;

import com.example.progettoschedly.dto.HistoryDTO;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface HistoryService {
    List<HistoryDTO> getByPiano(Long pianoId);

    List<HistoryDTO> getByUser(Long userId);

    HistoryDTO createForPiano(Long pianoId, HistoryDTO dto);

    boolean delete(Long id);
}

