package com.gpa.tributario.gerencial.application.eventoObrigacoes.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventoObrigacoesTotaisResponse {

    private String UF;
    private Long quantidade;
    private Long concluidos;
    private Long pendentes;
}
