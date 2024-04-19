package com.gpa.tributario.gerencial.application.faturamento.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FaturamentoResponse {

    private String id;
    private LocalDate data;
    private String uf;
    private Double faturamento;
    private Double icmsVendas;
}
