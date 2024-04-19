package com.gpa.tributario.gerencial.application.fechamento.response;

import com.gpa.tributario.gerencial.enuns.EmpresaEnum;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FechamentoResponse {

    private String id;
    private Integer idLancamento;
    private LocalDate dataFechamento;
    private String ufOperacao;
    private String lancamento;
    private String log;
    private String ufResponsavelPgto;
    private boolean lancamentoConferido;
    private String analistaExecutor;
    private String analistaValidador;
    private String responsavelInput;
    private EmpresaEnum empresa;

    private String tipo;
    private String tributacaoRelativa;
    private boolean monetizacao;
    private boolean impactaFechamento;
    private LocalDate fechamento;
    private LocalDate competencia;
    private LocalDate caixa;
    private Double valorTributarioApuracao;
    private Double ajustePositivo;
    private Double ajusteNegativo;
    private Double valorTributarioCompliance;
    private String conferenciaValores;
    private Double ajusteValorProcessadoUtilizado;
    private String observacoesGerais;
    private String qualAcaoRealizar;
}
