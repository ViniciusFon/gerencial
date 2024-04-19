package com.gpa.tributario.gerencial.application.monetizacao.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonetizacaoRequest {

    @NotNull
    private LocalDate data;
    @NotNull
    @Size(min = 2, max = 2)
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
