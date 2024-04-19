package com.gpa.tributario.gerencial.application.relatorio.response;

import com.gpa.tributario.gerencial.enuns.ScoreEnum;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioResponse {

    private LocalDate data;
    private String uf;
    private Long qtdeFiliais;
    private Double faturamento;
    private Integer porcentFatTotal;
    private Double faturamentoMesAnterior;
    private Integer porcentFatMesAnterior;
    private Double icmsVenda;
    private Double porcentIcms;
    private Double icmsProprio;
    private Double icmsST;
    private Double porcentSomaIcmsPorFat;
    private Double valorRecolhidoMes;
    private Double percentFatVsRec;
    private Integer qtdeArqValid;
    private Double saldoCredorApuracao;
    private Double saldoDevedorApuracao;
    private Double beneficios;
    private Long qtdeNFRecebida;
    private Long qtdeNFEmitida;
    private Long ctes;
    private Long nfDevolvidas;
    private Long nfReversao;
    private boolean regimeEspecial;
    private ScoreEnum scoreFiscalizacao;
    private Long obrigacoesAcessorias;
    private Double saldoAnteriorContab;
    private Double ajustes;
    private Double movimentacao;
    private Double monetizacao;
    private Double saldoFinalContab;
    private Double saldoEstimado;
    private Double saldoRealizado;
    private Double saldoCredorIPI;
    private Double saldoDevedorIPI;
}
