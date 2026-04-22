package com.example.progettoschedly.data.repository;

import com.example.progettoschedly.data.entity.Piano;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PianoRepository extends JpaRepository<Piano, Long> {

    Optional<Piano> findByUtenteId(Long utenteId);

    List<Piano> findAllByUtenteId(Long utenteId);

    List<Piano> findAllByUtenteIdOrderByDataCreazioneDesc(Long utenteId);

    @Modifying
    @Query("DELETE FROM Piano p WHERE p.utente.id = :userId")
    void deleteAllByUtenteId(@Param("userId") Long userId);
}
