package com.gpa.tributario.gerencial.application.nota.response;

import com.gpa.tributario.gerencial.enuns.EmpresaEnum;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotaResponse {

    private String id;
    private LocalDate dataNota;
    private EmpresaEnum empresa;
    private String uf;
    private Integer cfop;
    private String emitidarecebida;
    private Long qtdeNfs;
    private Double valorTotal;
    private Long ctes;
}
