package com.example.progettoschedly.controller;

import com.example.progettoschedly.data.entity.Esp;
import com.example.progettoschedly.data.entity.Notifica;
import com.example.progettoschedly.data.entity.Piano;
import com.example.progettoschedly.data.entity.Tasks;
import com.example.progettoschedly.data.repository.EspRepository;
import com.example.progettoschedly.data.repository.NotificaRepository;
import com.example.progettoschedly.data.repository.PianoRepository;
import com.example.progettoschedly.data.repository.TasksRepository;
import com.example.progettoschedly.data.service.NotificaService;
import com.example.progettoschedly.data.service.PianoService;
import com.example.progettoschedly.dto.EspDTO;
import com.example.progettoschedly.data.service.EspService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class EspController {

    private final EspService espService;
    private final PianoRepository pianoRepository;
    private final EspRepository espRepository;
    private final NotificaRepository notificaRepository;
    private final TasksRepository tasksRepository;


    public EspController(EspService espService, PianoRepository pianoRepository, EspRepository espRepository, NotificaRepository notificaRepository, TasksRepository tasksRepository) {
        this.espService = espService;
        this.pianoRepository = pianoRepository;
        this.espRepository = espRepository;
        this.notificaRepository = notificaRepository;
        this.tasksRepository = tasksRepository;
    }

    @GetMapping("/piani/{pianoId}/esportazioni")
    public List<EspDTO> getByPiano(@PathVariable Long pianoId) {
        return espService.getByPiano(pianoId);
    }

    @PostMapping("/piani/{pianoId}/esportazioni")
    public ResponseEntity<Void> logEsportazione(@PathVariable Long pianoId,
                                                @RequestBody Map<String, String> body) {

        String formato = body.getOrDefault("formato", "UNKNOWN").toUpperCase();


        Piano piano = pianoRepository.findById(pianoId)
                .orElseThrow(() -> new IllegalArgumentException("Piano non trovato"));

        Esp esp = new Esp();
        esp.setDataEsportazione(LocalDateTime.now());
        esp.setPiano(piano);
        esp.setFormato(formato);
        espRepository.save(esp);


        Optional<Tasks> anyTaskOpt = tasksRepository.findFirstByPianoIdOrderByGiornoAscFasciaOrariaAsc(pianoId);
        if (anyTaskOpt.isPresent()) {
            Tasks t = anyTaskOpt.get();

            Notifica n = new Notifica();
            n.setTipo("ESPORTAZIONE_" + formato);
            n.setAttivita(t);
            n.setUtente(t.getUtente());
            n.setOrarioNotifica(LocalTime.now().withSecond(0).withNano(0));
            notificaRepository.save(n);
        }

        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/esportazioni/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = espService.delete(id);
        if (!deleted) {
            return ResponseEntity.<Void>notFound().build();
        }
        return ResponseEntity.<Void>noContent().build();
    }
}

