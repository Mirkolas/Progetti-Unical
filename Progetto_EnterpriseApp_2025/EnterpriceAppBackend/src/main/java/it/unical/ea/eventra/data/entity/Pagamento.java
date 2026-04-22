package it.unical.ea.eventra.data.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import it.unical.ea.eventra.data.constants.StatoPagamento;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_titolare")
    private String nomeTitolare;

    @Column(name = "cognome_titolare")
    private String cognomeTitolare;

    @Column(name = "numero_carta")
    private String numeroCarta;

    @Column(name = "scadenza")
    private String scadenza;

    @Column(name = "cvv")
    private String cvv;

    @Column(name = "importo")
    private BigDecimal importo;

    @Column(name = "data_pagamento")
    private LocalDateTime dataPagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "stato")
    private StatoPagamento stato;

    @OneToOne
    @JoinColumn(name = "ordine_id")
    private Ordine ordine;
}
