package com.gpa.tributario.gerencial.application.icms.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IcmsResponse {

    private String id;
    private LocalDate dataIcms;
    private String codEstado;
    private Integer cfop;
    private String descricao;
    private Double valorContab;
    private Double baseTribu;
    private Double valorIcms;
    private Double baseIsenta;
    private Double baseOutras;
}
