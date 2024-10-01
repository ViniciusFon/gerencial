package com.gpa.tributario.gerencial.application.eventoObrigacoes.request;

import com.gpa.tributario.gerencial.enuns.EmpresaEnum;
import com.gpa.tributario.gerencial.enuns.ObrigacaoEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventoObrigacoesRequest {

    @NotNull
    private String estado;
    private String ufDestino;
    @NotNull
    private EmpresaEnum empresa;
    @NotNull
    @NotBlank
    private String loja;
    @NotNull
    @NotBlank
    private String ie;
    @NotNull
    @NotBlank
    private String cnpj;
    @NotNull
    @NotBlank
    private ObrigacaoEnum obrigacao;

}
