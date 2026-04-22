package com.example.progettoschedly.controller;

import com.example.progettoschedly.dto.NotificaDTO;
import com.example.progettoschedly.data.service.NotificaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class NotificaController {

    private final NotificaService notificaService;

    public NotificaController(NotificaService notificaService) {
        this.notificaService = notificaService;
    }

    @GetMapping("/utenti/{userId}/notifiche")
    public List<NotificaDTO> getByUser(@PathVariable Long userId) {
        return notificaService.getByUser(userId);
    }

    @GetMapping("/tasks/{taskId}/notifiche")
    public List<NotificaDTO> getByTask(@PathVariable Long taskId) {
        return notificaService.getByTask(taskId);
    }

    @PostMapping("/notifiche")
    public ResponseEntity<NotificaDTO> create(@RequestBody NotificaDTO dto) {
        return ResponseEntity.ok(notificaService.create(dto));
    }

    @PutMapping("/notifiche/{id}")
    public ResponseEntity<NotificaDTO> update(@PathVariable Long id, @RequestBody NotificaDTO dto) {
        return notificaService.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/notifiche/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = notificaService.delete(id);
        if (!deleted) {
            return ResponseEntity.<Void>notFound().build();
        }
        return ResponseEntity.<Void>noContent().build();
    }
}

