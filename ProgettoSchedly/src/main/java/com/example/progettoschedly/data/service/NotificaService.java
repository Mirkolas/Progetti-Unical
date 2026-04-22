package com.example.progettoschedly.data.service;

import com.example.progettoschedly.dto.NotificaDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public interface NotificaService {
    List<NotificaDTO> getByUser(Long userId);

    List<NotificaDTO> getByTask(Long taskId);

    NotificaDTO create(NotificaDTO dto);

    Optional<NotificaDTO> update(Long id, NotificaDTO dto);

    boolean delete(Long id);
}

