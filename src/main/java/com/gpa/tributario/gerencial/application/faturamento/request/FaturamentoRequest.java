package com.gpa.tributario.gerencial.application.faturamento.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FaturamentoRequest {

    @NotNull
    private LocalDate data;
    @NotNull
    @NotBlank
    private String uf;
    private Double faturamento;

}
