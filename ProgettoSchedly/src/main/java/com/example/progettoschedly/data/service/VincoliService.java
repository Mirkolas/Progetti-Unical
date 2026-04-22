package com.example.progettoschedly.data.service;

import com.example.progettoschedly.dto.VincoliDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public interface VincoliService {
    Optional<VincoliDTO> getByUser(Long userId);

    VincoliDTO upsertForUser(Long userId, VincoliDTO dto);
}

