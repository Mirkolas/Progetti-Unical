package com.example.progettoschedly.data.repository;

import com.example.progettoschedly.data.entity.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TasksRepository extends JpaRepository<Tasks, Long> {

    List<Tasks> findByUtenteId(Long utenteId);

    List<Tasks> findByUtenteIdAndPianoId(Long utenteId, Long pianoId);

    List<Tasks> findAllByPianoId(Long pianoId);

    @Modifying
    @Query("DELETE FROM Tasks t WHERE t.utente.id = :userId")
    void deleteAllByUtenteId(@Param("userId") Long userId);

    Optional<Tasks> findFirstByPianoIdOrderByGiornoAscFasciaOrariaAsc(Long pianoId);
}
