package com.example.progettoschedly.controller;

import com.example.progettoschedly.dto.VincoliDTO;
import com.example.progettoschedly.dto.UtenteDTO;
import com.example.progettoschedly.data.service.VincoliService;
import com.example.progettoschedly.data.service.UtenteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class VincoliController {

    private final VincoliService vincoliService;
    private final UtenteService utenteService;

    public VincoliController(VincoliService vincoliService, UtenteService utenteService) {
        this.vincoliService = vincoliService;
        this.utenteService = utenteService;
    }

    @GetMapping("/vincoli/current")
    public ResponseEntity<VincoliDTO> getCurrentUserVincoli() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return ResponseEntity.status(401).build();
        }
        String username = auth.getName();
        return utenteService.getByUsername(username)
                .map(user -> vincoliService.getByUser(user.getId())
                        .map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/vincoli/current")
    public ResponseEntity<VincoliDTO> upsertCurrentUserVincoli(@RequestBody VincoliDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return ResponseEntity.status(401).build();
        }
        String username = auth.getName();
        return utenteService.getByUsername(username)
                .map(user -> {
                    VincoliDTO saved = vincoliService.upsertForUser(user.getId(), dto);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/utenti/{userId}/vincoli")
    public ResponseEntity<VincoliDTO> getForUser(@PathVariable Long userId) {
        return vincoliService.getByUser(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/utenti/{userId}/vincoli")
    public ResponseEntity<VincoliDTO> upsertForUser(@PathVariable Long userId,
                                                   @RequestBody VincoliDTO dto) {
        VincoliDTO saved = vincoliService.upsertForUser(userId, dto);
        return ResponseEntity.ok(saved);
    }
}
