package com.gpa.tributario.gerencial.application.faturamento.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GraficoFaturamentoResponse {

    private LocalDate data;
    private double total;
}
