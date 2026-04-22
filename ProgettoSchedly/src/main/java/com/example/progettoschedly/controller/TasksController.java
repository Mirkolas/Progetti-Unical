package com.example.progettoschedly.controller;

import com.example.progettoschedly.dto.TaskDTO;
import com.example.progettoschedly.dto.UtenteDTO;
import com.example.progettoschedly.data.service.TaskService;
import com.example.progettoschedly.data.service.UtenteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TasksController {

    private final TaskService taskService;
    private final UtenteService utenteService;

    public TasksController(TaskService taskService, UtenteService utenteService) {
        this.taskService = taskService;
        this.utenteService = utenteService;
    }

    @GetMapping("/tasks/current")
    public ResponseEntity<List<TaskDTO>> getCurrentUserTasks() {
        Optional<UtenteDTO> userOpt = getLoggedUser();
        if (userOpt.isEmpty()) return ResponseEntity.status(401).build();

        List<TaskDTO> tasks = taskService.getByUser(userOpt.get().getId());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/piani/{pianoId}/tasks")
    public ResponseEntity<List<TaskDTO>> getTasksForCurrentUserByPiano(@PathVariable Long pianoId) {
        Optional<UtenteDTO> userOpt = getLoggedUser();
        if (userOpt.isEmpty()) return ResponseEntity.status(401).build();

        List<TaskDTO> tasks = taskService.getByUserAndPiano(userOpt.get().getId(), pianoId);
        return ResponseEntity.ok(tasks);
    }

    @DeleteMapping("/tasks/current/all")
    public ResponseEntity<Void> deleteAllCurrentUserTasks() {
        Optional<UtenteDTO> userOpt = getLoggedUser();
        if (userOpt.isEmpty()) return ResponseEntity.status(401).build();

        taskService.deleteAllByUser(userOpt.get().getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/utenti/{userId}/tasks")
    public List<TaskDTO> getTasksForUser(@PathVariable Long userId) {
        return taskService.getByUser(userId);
    }

    @PostMapping("/utenti/{userId}/tasks")
    public ResponseEntity<TaskDTO> createTask(@PathVariable Long userId,
                                              @RequestBody TaskDTO task) {
        TaskDTO saved = taskService.createForUser(userId, task);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/tasks/current/bulk")
    public ResponseEntity<List<TaskDTO>> createTasksBulkForCurrentUser(@RequestBody List<TaskDTO> tasks) {
        Optional<UtenteDTO> userOpt = getLoggedUser();
        if (userOpt.isEmpty()) return ResponseEntity.status(401).build();

        List<TaskDTO> saved = taskService.createBulkForUser(userOpt.get().getId(), tasks);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/piani/{pianoId}/tasks/bulk")
    public ResponseEntity<List<TaskDTO>> createTasksBulkForCurrentUserOnPiano(@PathVariable Long pianoId,
                                                                              @RequestBody List<TaskDTO> tasks) {
        Optional<UtenteDTO> userOpt = getLoggedUser();
        if (userOpt.isEmpty()) return ResponseEntity.status(401).build();

        List<TaskDTO> saved = taskService.createBulkForUserAndPiano(userOpt.get().getId(), pianoId, tasks);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/utenti/{userId}/tasks/bulk")
    public ResponseEntity<List<TaskDTO>> createTasksBulk(@PathVariable Long userId,
                                                         @RequestBody List<TaskDTO> tasks) {
        List<TaskDTO> saved = taskService.createBulkForUser(userId, tasks);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody TaskDTO updated) {
        return taskService.update(id, updated)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        boolean deleted = taskService.delete(id);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }

    private Optional<UtenteDTO> getLoggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return Optional.empty();
        }
        return utenteService.getByUsername(auth.getName());
    }
}
