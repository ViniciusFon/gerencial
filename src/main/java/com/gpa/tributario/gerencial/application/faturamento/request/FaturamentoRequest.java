package com.gpa.tributario.gerencial.application.faturamento.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FaturamentoRequest {

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate data;
    @NotNull
    @NotBlank
    @Size(min = 2, max = 2)
    private String uf;
    private Double faturamento;

}
