package com.example.progettoschedly.data.service;

import com.example.progettoschedly.dto.TaskDTO;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    List<TaskDTO> getByUser(Long userId);

    List<TaskDTO> getByUserAndPiano(Long userId, Long pianoId);

    TaskDTO createForUser(Long userId, TaskDTO dto);

    List<TaskDTO> createBulkForUser(Long userId, List<TaskDTO> dtos);

    List<TaskDTO> createBulkForUserAndPiano(Long userId, Long pianoId, List<TaskDTO> dtos);

    Optional<TaskDTO> update(Long id, TaskDTO dto);

    boolean delete(Long id);

    boolean deleteAllByUser(Long userId);
}
