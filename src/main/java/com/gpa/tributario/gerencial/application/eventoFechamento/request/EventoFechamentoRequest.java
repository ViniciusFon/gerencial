package com.gpa.tributario.gerencial.application.eventoFechamento.request;

import com.gpa.tributario.gerencial.enuns.EmpresaEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventoFechamentoRequest {

    @NotNull
    private String estado;
    @NotNull
    private EmpresaEnum empresa;
    @NotNull
    @NotBlank
    private String nomeRelatorio;

}
