package com.gpa.tributario.gerencial.entity;

import com.gpa.tributario.gerencial.enuns.EmpresaEnum;
import com.gpa.tributario.gerencial.enuns.ObrigacaoEnum;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "obrigacoes")
public class Obrigacoes {

    @Id
    private String id;
    private String UF;
    private String ufDestino;
    private String nome;
    private EmpresaEnum empresa;
    private String loja;
    private String ie;
    private String cnpj;
    private ObrigacaoEnum obrigacao;
    private boolean concluido;
    private List<HistoricoEventoObrigacoes> historico;

    public void addHistorico(HistoricoEventoObrigacoes hist){
        if(this.historico == null){
            this.historico = new ArrayList<>();
        }

        this.historico.add(hist);
    }
}
