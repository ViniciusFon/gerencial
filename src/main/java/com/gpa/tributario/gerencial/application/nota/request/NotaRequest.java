package com.gpa.tributario.gerencial.application.nota.request;

import com.gpa.tributario.gerencial.enuns.EmpresaEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotaRequest {

    @NotNull
    private LocalDate dataNota;
    @NotNull
    private EmpresaEnum empresa;
    @NotNull
    @Size(min = 2, max = 2)
    private String uf;
    @NotNull
    private Integer cfop;
    private String emitidarecebida;
    private Long qtdeNfs;
    private Double valorTotal;
    private Long ctes;
}
