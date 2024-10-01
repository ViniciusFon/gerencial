package com.gpa.tributario.gerencial.application.eventoObrigacoes.response;

import com.gpa.tributario.gerencial.enuns.EmpresaEnum;
import com.gpa.tributario.gerencial.enuns.ObrigacaoEnum;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventoObrigacoesResponse {

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
}
