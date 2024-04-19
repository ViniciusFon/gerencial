package com.gpa.tributario.gerencial.application.fechamento.request;

import com.gpa.tributario.gerencial.enuns.EmpresaEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FechamentoRequest {

    @NotNull
    private Integer idLancamento;
    @NotNull
    private LocalDate dataFechamento;
    @NotNull
    @Size(min = 2, max = 2)
    private String ufOperacao;
    private String log;
    @NotNull
    @Size(min = 2, max = 2)
    private String ufResponsavelPgto;
    private boolean lancamentoConferido;
    @NotNull
    @NotBlank
    private String analistaExecutor;
    @NotNull
    @NotBlank
    private String analistaValidador;
    @NotNull
    @NotBlank
    private String responsavelInput;
    @NotNull
    private EmpresaEnum empresa;
    private String lancamento;
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
