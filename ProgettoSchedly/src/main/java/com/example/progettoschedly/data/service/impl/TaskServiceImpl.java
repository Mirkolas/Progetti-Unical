package com.example.progettoschedly.data.service.impl;

import com.example.progettoschedly.data.entity.Notifica;
import com.example.progettoschedly.data.entity.Piano;
import com.example.progettoschedly.data.entity.Tasks;
import com.example.progettoschedly.data.entity.Utente;
import com.example.progettoschedly.data.repository.NotificaRepository;
import com.example.progettoschedly.data.repository.PianoRepository;
import com.example.progettoschedly.data.repository.TasksRepository;
import com.example.progettoschedly.data.repository.UtenteRepository;
import com.example.progettoschedly.dto.TaskDTO;
import com.example.progettoschedly.data.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskServiceImpl implements TaskService {

    private static final String STATO_ELIMINATA = "eliminata";

    private final TasksRepository tasksRepository;
    private final UtenteRepository utenteRepository;
    private final PianoRepository pianoRepository;
    private final NotificaRepository notificaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getByUser(Long userId) {
        return tasksRepository.findByUtenteId(userId).stream()
                .filter(t -> !isEliminata(t.getStato()))
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<TaskDTO> getByUserAndPiano(Long userId, Long pianoId) {
        return List.of();
    }

    @Override
    public TaskDTO createForUser(Long userId, TaskDTO dto) {
        Utente utente = utenteRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        Tasks entity = new Tasks();
        applyFields(entity, dto);
        entity.setUtente(utente);
        applyPiano(entity, dto.getPianoId());

        Tasks saved = tasksRepository.save(entity);

        upsertNotifica(saved, utente, "CREAZIONE");

        return toDto(saved);
    }

    @Override
    public List<TaskDTO> createBulkForUser(Long userId, List<TaskDTO> dtos) {
        Utente utente = utenteRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        List<Tasks> entities = dtos.stream().map(dto -> {
            Tasks t = new Tasks();
            applyFields(t, dto);
            t.setUtente(utente);
            applyPiano(t, dto.getPianoId());
            return t;
        }).toList();

        List<Tasks> saved = tasksRepository.saveAll(entities);

        for (Tasks t : saved) {
            upsertNotifica(t, utente, "CREAZIONE");
        }

        return saved.stream().map(this::toDto).toList();
    }

    @Override
    public List<TaskDTO> createBulkForUserAndPiano(Long userId, Long pianoId, List<TaskDTO> dtos) {
        return List.of();
    }

    @Override
    public Optional<TaskDTO> update(Long id, TaskDTO dto) {
        return tasksRepository.findById(id)
                .map(found -> {
                    var oldGiorno = found.getGiorno();
                    var oldFascia = found.getFasciaOraria();
                    var oldStato = found.getStato();

                    applyFields(found, dto);
                    applyPiano(found, dto.getPianoId());

                    Tasks saved = tasksRepository.save(found);

                    boolean moved =
                            !Objects.equals(oldGiorno, saved.getGiorno())
                                    || !Objects.equals(oldFascia, saved.getFasciaOraria());

                    boolean isStatusChange = !Objects.equals(oldStato, saved.getStato());

                    if (moved && !isEliminata(saved.getStato()) && !isStatusChange) {
                        upsertNotifica(saved, saved.getUtente(), "SPOSTAMENTO");
                    }

                    return toDto(saved);
                });
    }

    @Override
    public boolean delete(Long id) {
        Tasks task = tasksRepository.findById(id).orElse(null);
        if (task == null) return false;

        task.setStato(STATO_ELIMINATA);
        Tasks saved = tasksRepository.save(task);

        upsertNotifica(saved, saved.getUtente(), "ELIMINAZIONE");

        return true;
    }

    @Override
    public boolean deleteAllByUser(Long userId) {
        List<Tasks> tasks = tasksRepository.findByUtenteId(userId);
        if (tasks.isEmpty()) return true;

        for (Tasks t : tasks) {
            if (isEliminata(t.getStato())) continue;
            t.setStato(STATO_ELIMINATA);
        }

        List<Tasks> saved = tasksRepository.saveAll(tasks);

        for (Tasks t : saved) {
            if (isEliminata(t.getStato())) {
                upsertNotifica(t, t.getUtente(), "ELIMINAZIONE");
            }
        }

        return true;
    }


    private void upsertNotifica(Tasks task, Utente utente, String tipo) {
        if (task == null || task.getId() == null) return;

        LocalTime orario = parseOrNow(task.getFasciaOraria());

        Optional<Notifica> existing = notificaRepository.findByAttivitaIdAndTipoIgnoreCase(task.getId(), tipo);

        if (existing.isPresent()) {
            Notifica n = existing.get();
            n.setOrarioNotifica(orario);
            n.setUtente(utente);
            n.setAttivita(task);
            n.setTipo(tipo);
            notificaRepository.save(n);
        } else {
            Notifica n = new Notifica();
            n.setTipo(tipo);
            n.setOrarioNotifica(orario);
            n.setAttivita(task);
            n.setUtente(utente);
            notificaRepository.save(n);
        }
    }

    private boolean isEliminata(String stato) {
        if (stato == null) return false;
        return STATO_ELIMINATA.equalsIgnoreCase(stato.trim());
    }

    private LocalTime parseOrNow(String fasciaOraria) {
        try {
            if (fasciaOraria == null || fasciaOraria.isBlank()) {
                return LocalTime.now().withSecond(0).withNano(0);
            }
            return LocalTime.parse(fasciaOraria.trim());
        } catch (Exception e) {
            return LocalTime.now().withSecond(0).withNano(0);
        }
    }

    private void applyFields(Tasks entity, TaskDTO dto) {
        entity.setTitolo(dto.getTitolo());
        entity.setDurata(dto.getDurata());
        entity.setGiorno(dto.getGiorno());
        entity.setFasciaOraria(dto.getFasciaOraria());
        entity.setScadenza(dto.getScadenza());
        entity.setPriorita(dto.getPriorita());
        entity.setNote(dto.getNote());
        entity.setStato(dto.getStato());
    }

    private void applyPiano(Tasks entity, Long pianoId) {
        if (pianoId == null) {
            entity.setPiano(null);
            return;
        }
        Optional<Piano> piano = pianoRepository.findById(pianoId);
        piano.ifPresent(entity::setPiano);
    }

    private TaskDTO toDto(Tasks entity) {
        TaskDTO dto = new TaskDTO();
        dto.setId(entity.getId());
        dto.setTitolo(entity.getTitolo());
        dto.setDurata(entity.getDurata());
        dto.setGiorno(entity.getGiorno());
        dto.setFasciaOraria(entity.getFasciaOraria());
        dto.setScadenza(entity.getScadenza());
        dto.setPriorita(entity.getPriorita());
        dto.setNote(entity.getNote());
        dto.setStato(entity.getStato());
        dto.setUtenteId(entity.getUtente() != null ? entity.getUtente().getId() : null);
        dto.setPianoId(entity.getPiano() != null ? entity.getPiano().getId() : null);
        return dto;
    }
}
