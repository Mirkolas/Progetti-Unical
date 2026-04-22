package com.example.progettoschedly.data.repository;



import com.example.progettoschedly.data.entity.Vincoli;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VincoliRepository extends JpaRepository<Vincoli, Long> {
    Vincoli findByUtenteId(Long utenteId);
}
