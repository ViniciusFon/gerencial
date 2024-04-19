package com.gpa.tributario.gerencial.application.apuracao.response;

import com.gpa.tributario.gerencial.enuns.EmpresaEnum;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApuracaoResponse {

    private String id;
    private LocalDate data;
    private EmpresaEnum empresa;
    private String uf;
    private double saldoCreditAnterior;
    private double saida;
    private double outroDebito;
    private double tDebito;
    private double entrada;
    private double outroCredito;
    private double tCredito;
    private double deducaoRedutora;
    private double deducaoCredora;
    private double saldoCredor;
    private double saldoDevedor;
    private double ressarcimentoProprio;
    private double ressarcimentoST;
    private double centralizacaoDebito;
    private double centralizacaoCredito;
    private double concentracaoDebito;
    private double concentracaoCredito;
    private double totalPagtoRealizadoFechado;
}
