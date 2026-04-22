package it.unical.ea.eventra.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.unical.ea.eventra.data.constants.StatoPagamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PagamentoDTO {
    private Long id;
    private String nomeTitolare;
    private String cognomeTitolare;
    private String numeroCarta;

    private String scadenza;

    private String cvv;
    private BigDecimal importo;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataPagamento;

    private StatoPagamento stato;
    private Long ordineId;
}
