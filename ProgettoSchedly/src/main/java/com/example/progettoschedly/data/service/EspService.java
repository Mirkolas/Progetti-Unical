package com.example.progettoschedly.data.service;

import com.example.progettoschedly.dto.EspDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EspService {
    List<EspDTO> getByPiano(Long pianoId);

    EspDTO createForPiano(Long pianoId, EspDTO dto);

    boolean delete(Long id);
}

