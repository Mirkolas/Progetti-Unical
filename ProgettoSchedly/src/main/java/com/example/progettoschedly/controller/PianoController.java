package com.example.progettoschedly.controller;

import com.example.progettoschedly.dto.PianoDTO;
import com.example.progettoschedly.dto.UtenteDTO;
import com.example.progettoschedly.data.service.PianoService;
import com.example.progettoschedly.data.service.UtenteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PianoController {

    private final PianoService pianoService;
    private final UtenteService utenteService;

    public PianoController(PianoService pianoService, UtenteService utenteService) {
        this.pianoService = pianoService;
        this.utenteService = utenteService;
    }

    @GetMapping("/piani")
    public List<PianoDTO> getAll() {
        return pianoService.getAll();
    }

    @GetMapping("/piani/current")
    public ResponseEntity<List<PianoDTO>> getAllCurrentUserPiani() {
        Optional<UtenteDTO> userOpt = getLoggedUser();
        if (userOpt.isEmpty()) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(pianoService.getAllByUser(userOpt.get().getId()));
    }

    @PostMapping("/piani")
    public ResponseEntity<PianoDTO> createPianoForCurrentUser(@RequestBody PianoDTO dto) {
        Optional<UtenteDTO> userOpt = getLoggedUser();
        if (userOpt.isEmpty()) return ResponseEntity.status(401).build();

        PianoDTO saved = pianoService.createForUser(userOpt.get().getId(), dto);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/piani/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = pianoService.delete(id);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/piani/current")
    public ResponseEntity<Void> deleteAllCurrentUserPiani() {
        Optional<UtenteDTO> userOpt = getLoggedUser();
        if (userOpt.isEmpty()) return ResponseEntity.status(401).build();

        pianoService.deleteAllByUser(userOpt.get().getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/piano/current")
    public ResponseEntity<PianoDTO> getCurrentUserPiano() {
        Optional<UtenteDTO> userOpt = getLoggedUser();
        if (userOpt.isEmpty()) return ResponseEntity.status(401).build();

        return pianoService.getByUser(userOpt.get().getId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/piano/current")
    public ResponseEntity<PianoDTO> createCurrentUserPiano(@RequestBody PianoDTO dto) {
        Optional<UtenteDTO> userOpt = getLoggedUser();
        if (userOpt.isEmpty()) return ResponseEntity.status(401).build();

        PianoDTO saved = pianoService.createForUser(userOpt.get().getId(), dto);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/piano/current")
    public ResponseEntity<PianoDTO> upsertCurrentUserPiano(@RequestBody PianoDTO dto) {
        return createCurrentUserPiano(dto);
    }

    @DeleteMapping("/piano/current")
    public ResponseEntity<Void> deleteCurrentUserPianoSingular() {
        return deleteAllCurrentUserPiani();
    }

    @DeleteMapping("/piano/current/all")
    public ResponseEntity<Void> deleteAllCurrentUserPianiLegacy() {
        return deleteAllCurrentUserPiani();
    }

    @GetMapping("/piani/{id}")
    public ResponseEntity<PianoDTO> getById(@PathVariable Long id) {
        return pianoService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private Optional<UtenteDTO> getLoggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return Optional.empty();
        }
        return utenteService.getByUsername(auth.getName());
    }
}
