package com.gpa.tributario.gerencial.application.eventoFechamento.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventoFechamentoTotaisResponse {

    private String UF;
    private Long quantidade;
    private Long concluidos;
    private Long pendentes;
}
