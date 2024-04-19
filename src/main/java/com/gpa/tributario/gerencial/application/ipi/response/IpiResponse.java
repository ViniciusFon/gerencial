package com.gpa.tributario.gerencial.application.ipi.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IpiResponse {

    private String id;
    private LocalDate data;
    private String uf;
    private Integer loja;
    private Double saldoCreditAnterior;
    private Double debitoSaida;
    private Double debitoOutro;
    private Double debitoTdebito;
    private Double creditoEntrada;
    private Double creditoOutro;
    private Double creditoTcredito;
    private Double deducaoRedutora;
    private Double deducaoCredora;
    private Double saldoCredor;
    private Double saldoDevedor;
}
