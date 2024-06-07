package com.gpa.tributario.gerencial.dto;

import com.gpa.tributario.gerencial.application.eventoFechamento.response.EventoFechamentoResponse;
import com.gpa.tributario.gerencial.application.eventoFechamento.response.EventoFechamentoTotaisResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketFechamentoDto {
    private List<EventoFechamentoTotaisResponse> totais;
    private List<EventoFechamentoResponse> relatorios;
}
