package it.unical.ea.eventra.dto;

import it.unical.ea.eventra.data.entity.Biglietto;
import it.unical.ea.eventra.data.entity.TipoPosto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoEditDTO {

    private Long id;

    private String nome;
    private String descrizione;
    private LocalDateTime dataOraEvento;
    private LocalDateTime dataOraAperturaCancelli;
    private Integer postiDisponibili;

}
