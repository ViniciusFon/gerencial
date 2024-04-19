package com.gpa.tributario.gerencial.application.apuracao.request;

import com.gpa.tributario.gerencial.enuns.EmpresaEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApuracaoRequest {

    @NotNull
    private LocalDate data;
    @NotNull
    private EmpresaEnum empresa;
    @NotNull
    @Size(min = 2, max = 2)
    private String uf;
    private Double saldoCreditAnterior;
    private Double saida;
    private Double outroDebito;
    private Double tDebito;
    private Double entrada;
    private Double outroCredito;
    private Double tCredito;
    private Double deducaoRedutora;
    private Double deducaoCredora;
    private Double saldoCredor;
    private Double saldoDevedor;
    private Double ressarcimentoProprio;
    private Double ressarcimentoST;
    private Double centralizacaoDebito;
    private Double centralizacaoCredito;
    private Double concentracaoDebito;
    private Double concentracaoCredito;
    private Double totalPagtoRealizadoFechado;
}
