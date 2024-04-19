package com.gpa.tributario.gerencial.application.monetizacao.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonetizacaoResponse {

    private String id;
    private LocalDate data;
    private String uf;
    private double saldoAnterior;
    private double ajuste;
    private double movimentacao;
    private double monetizacao;
    private double saldoFinal;
    private String filler;
    private double estimado;
    private double realizado;
}
