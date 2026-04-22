package com.example.progettoschedly.data.repository;


import com.example.progettoschedly.data.entity.Esp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EspRepository extends JpaRepository<Esp, Long> {
    List<Esp> findByPianoId(Long pianoId);
    
    @Modifying
    @Query("DELETE FROM Esp e WHERE e.piano.id IN :pianoIds")
    void deleteAllByPianoIds(@Param("pianoIds") List<Long> pianoIds);

    @Modifying
    @Query("DELETE FROM Esp e WHERE e.piano.utente.id = :userId")
    void deleteAllByUtenteId(@Param("userId") Long userId);

}
