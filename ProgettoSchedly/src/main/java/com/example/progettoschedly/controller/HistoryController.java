package com.example.progettoschedly.controller;

import com.example.progettoschedly.dto.HistoryDTO;
import com.example.progettoschedly.dto.UtenteDTO;
import com.example.progettoschedly.data.service.HistoryService;
import com.example.progettoschedly.data.service.UtenteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class HistoryController {

    private final HistoryService historyService;
    private final UtenteService utenteService;

    public HistoryController(HistoryService historyService, UtenteService utenteService) {
        this.historyService = historyService;
        this.utenteService = utenteService;
    }

    @GetMapping("/history/current")
    public ResponseEntity<List<HistoryDTO>> getCurrentUserHistory() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return ResponseEntity.status(401).build();
        }
        String username = auth.getName();
        return utenteService.getByUsername(username)
                .map(user -> ResponseEntity.ok(historyService.getByUser(user.getId())))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/piani/{pianoId}/history")
    public List<HistoryDTO> getByPiano(@PathVariable Long pianoId) {
        return historyService.getByPiano(pianoId);
    }

    @PostMapping("/piani/{pianoId}/history")
    public ResponseEntity<HistoryDTO> create(@PathVariable Long pianoId,
                                             @RequestBody HistoryDTO dto) {
        HistoryDTO saved = historyService.createForPiano(pianoId, dto);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/history/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = historyService.delete(id);
        if (!deleted) {
            return ResponseEntity.<Void>notFound().build();
        }
        return ResponseEntity.<Void>noContent().build();
    }
}

