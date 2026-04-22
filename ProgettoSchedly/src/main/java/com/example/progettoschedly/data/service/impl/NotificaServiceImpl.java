package com.example.progettoschedly.data.service.impl;

import com.example.progettoschedly.data.entity.Notifica;
import com.example.progettoschedly.data.entity.Tasks;
import com.example.progettoschedly.data.entity.Utente;
import com.example.progettoschedly.data.repository.NotificaRepository;
import com.example.progettoschedly.data.repository.TasksRepository;
import com.example.progettoschedly.data.repository.UtenteRepository;
import com.example.progettoschedly.dto.NotificaDTO;
import com.example.progettoschedly.data.service.NotificaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificaServiceImpl implements NotificaService {

    private final NotificaRepository notificaRepository;
    private final TasksRepository tasksRepository;
    private final UtenteRepository utenteRepository;

    @Override
    @Transactional(readOnly = true)
    public List<NotificaDTO> getByUser(Long userId) {
        return notificaRepository.findByUtenteId(userId).stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificaDTO> getByTask(Long taskId) {
        return notificaRepository.findByAttivitaId(taskId).stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public NotificaDTO create(NotificaDTO dto) {
        Notifica entity = new Notifica();
        applyFields(entity, dto);
        attachRelations(entity, dto);
        Notifica saved = notificaRepository.save(entity);

        return toDto(saved);

    }

    @Override
    public Optional<NotificaDTO> update(Long id, NotificaDTO dto) {
        return notificaRepository.findById(id)
                .map(found -> {
                    applyFields(found, dto);
                    attachRelations(found, dto);
                    Notifica saved = notificaRepository.save(found);
                    return toDto(saved);
                });
    }

    @Override
    public boolean delete(Long id) {
        if (!notificaRepository.existsById(id)) {
            return false;
        }
        notificaRepository.deleteById(id);
        return true;
    }

    private void applyFields(Notifica entity, NotificaDTO dto) {
        entity.setTipo(dto.getTipo());
        entity.setOrarioNotifica(dto.getOrarioNotifica());
    }

    private void attachRelations(Notifica entity, NotificaDTO dto) {
        if (dto.getAttivitaId() != null) {
            Tasks task = tasksRepository.findById(dto.getAttivitaId())
                    .orElseThrow(() -> new IllegalArgumentException("Attivita non trovata"));
            entity.setAttivita(task);
        } else {
            entity.setAttivita(null);
        }

        if (dto.getUtenteId() != null) {
            Utente utente = utenteRepository.findById(dto.getUtenteId())
                    .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
            entity.setUtente(utente);
        } else {
            entity.setUtente(null);
        }
    }

    private NotificaDTO toDto(Notifica entity) {
        NotificaDTO dto = new NotificaDTO();
        dto.setId(entity.getId());
        dto.setTipo(entity.getTipo());
        dto.setOrarioNotifica(entity.getOrarioNotifica());
        dto.setAttivitaId(entity.getAttivita() != null ? entity.getAttivita().getId() : null);
        dto.setUtenteId(entity.getUtente() != null ? entity.getUtente().getId() : null);
        return dto;
    }
}

