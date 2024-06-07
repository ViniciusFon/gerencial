package com.gpa.tributario.gerencial.application.eventoFechamento.response;

import com.gpa.tributario.gerencial.enuns.EmpresaEnum;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventoFechamentoResponse {

    private String id;
    private String UF;
    private String nome;
    private EmpresaEnum empresa;
    private String nomeRelatorio;
    private boolean concluido;
}
