package com.gpa.tributario.gerencial.application.icms.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IcmsRequest {

    @NotNull
    private LocalDate dataIcms;
    @NotNull
    @Size(min = 2, max = 2)
    private String codEstado;
    @NotNull
    private Integer cfop;
    private String descricao;
    private Double valorContab;
    private Double baseTribu;
    private Double valorIcms;
    private Double baseIsenta;
    private Double baseOutras;
}
