package com.gpa.tributario.gerencial.application.eventoFechamento.request;

import com.gpa.tributario.gerencial.enuns.EmpresaEnum;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventoFechamentoRequest {

    private String UF;
    private EmpresaEnum empresa;
    private String nomeRelatorio;

}
