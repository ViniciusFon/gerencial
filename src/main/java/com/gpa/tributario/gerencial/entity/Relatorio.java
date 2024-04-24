package com.gpa.tributario.gerencial.entity;

import com.gpa.tributario.gerencial.enuns.ScoreEnum;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "relatorio")
public class Relatorio {

    @Id
    private RelatorioID id;
    private long qtdeFiliais;
    private double faturamento;
    private int porcentFatTotal;
    private double faturamentoMesAnterior;
    private int porcentFatMesAnterior;
    private double icmsVenda;
    private double porcentIcms;
    private double icmsProprio;
    private double icmsST;
    private double porcentSomaIcmsPorFat;
    private double valorRecolhidoMes;
    private double percentFatVsRec;
    private int qtdeArqValid;
    private double saldoCredorApuracao;
    private double saldoDevedorApuracao;
    private double beneficios;
    private long qtdeNFRecebida;
    private long qtdeNFEmitida;
    private long ctes;
    private long nfDevolvidas;
    private long nfReversao;
    private boolean regimeEspecial;
    private ScoreEnum scoreFiscalizacao;
    private long obrigacoesAcessorias;
    private double saldoAnteriorContab;
    private double ajustes;
    private double movimentacao;
    private double monetizacao;
    private double saldoFinalContab;
    private double saldoEstimado;
    private double saldoRealizado;
    private double saldoCredorIPI;
    private double saldoDevedorIPI;

    public Relatorio(RelatorioID id){
        this.id = id;
    }

}
