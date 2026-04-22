package com.example.progettoschedly.data.repository;

import com.example.progettoschedly.data.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {
    List<History> findByPianoId(Long pianoId);

    @Query("SELECT h FROM History h WHERE h.piano.utente.id = :userId ORDER BY h.dataArchiviazione DESC")
    List<History> findByUtenteId(@Param("userId") Long userId);
    @Modifying
    @Query("DELETE FROM History h WHERE h.piano.utente.id = :userId")
    void deleteAllByUtenteId(@Param("userId") Long userId);


}
