package com.gpa.tributario.gerencial.entity;

import com.gpa.tributario.gerencial.enuns.EmpresaEnum;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "fechamento")
public class Fechamento {

    @Id
    private String id;
    private int idLancamento;
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
    private LocalDate competencia;
    private LocalDate caixa;
    private double valorTributarioApuracao;
    private double ajustePositivo;
    private double ajusteNegativo;
    private double valorTributarioCompliance;
    private String conferenciaValores;
    private double ajusteValorProcessadoUtilizado;
    private String observacoesGerais;
    private String qualAcaoRealizar;
}
