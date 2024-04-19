package com.gpa.tributario.gerencial.application.ipi.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IpiRequest {

    @NotNull
    private LocalDate data;
    @NotNull
    @Size(min = 2, max = 2)
    private String uf;
    @NotNull
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
