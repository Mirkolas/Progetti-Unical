package com.example.progettoschedly.data.service.impl;

import com.example.progettoschedly.data.entity.Notifica;
import com.example.progettoschedly.data.entity.Tasks;
import com.example.progettoschedly.data.entity.Utente;
import com.example.progettoschedly.data.repository.UtenteRepository;
import com.example.progettoschedly.data.service.UtenteService;
import com.example.progettoschedly.dto.UtenteDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UtenteServiceImpl implements UtenteService {

    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;

    private UtenteDTO toDto(Utente u){
        UtenteDTO dto = new UtenteDTO();

        dto.setId(u.getId());
        dto.setUsername(u.getUsername());
        dto.setEmail(u.getEmail());

        dto.setPassword(null);

        dto.setVincoliId(u.getVincoli() != null ? u.getVincoli().getId() : null);
        dto.setPianoId(
                u.getPiani() != null && !u.getPiani().isEmpty()
                        ? u.getPiani().get(u.getPiani().size() - 1).getId()
                        : null
        );


        dto.setAttivitaIds(
                u.getAttivita() == null ? List.of()
                        : u.getAttivita().stream().map(Tasks::getId).toList()
        );

        dto.setNotificheIds(
                u.getNotifiche() == null ? List.of()
                        : u.getNotifiche().stream().map(Notifica::getId).toList()
        );

        return dto;
    }

    private Utente fromDto(UtenteDTO dto){
        Utente u = new Utente();
        u.setId(dto.getId());
        u.setUsername(dto.getUsername());
        u.setEmail(dto.getEmail());
        u.setPassword(dto.getPassword());
        return u;
    }


    @Override
    public List<UtenteDTO> getAll() {
        return utenteRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public Optional<UtenteDTO> getById(Long id) {
        return utenteRepository.findById(id).map(this::toDto);
    }

    @Override
    public Optional<UtenteDTO> getByUsername(String username) {
        return utenteRepository.findByUsername(username).map(this::toDto);
    }

    @Override
    public UtenteDTO create(UtenteDTO dto) {
        Utente u = fromDto(dto);

        if (u.getPassword() != null && !u.getPassword().isBlank()) {
            u.setPassword(passwordEncoder.encode(u.getPassword()));
        }

        return toDto(utenteRepository.save(u));
    }

    @Override
    public Optional<UtenteDTO> update(Long id, UtenteDTO dto) {
        return utenteRepository.findById(id).map(existing -> {
            if (dto.getUsername() != null) existing.setUsername(dto.getUsername());
            if (dto.getEmail() != null) existing.setEmail(dto.getEmail());

            if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
                existing.setPassword(passwordEncoder.encode(dto.getPassword()));
            }

            return toDto(utenteRepository.save(existing));
        });
    }

    @Override
    public boolean delete(Long id) {
        if (!utenteRepository.existsById(id)) return false;
        utenteRepository.deleteById(id);
        return true;
    }
}
