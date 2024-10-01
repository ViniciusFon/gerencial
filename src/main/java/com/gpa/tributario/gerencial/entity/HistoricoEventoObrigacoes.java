package com.gpa.tributario.gerencial.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoricoEventoObrigacoes {

    private String userName;
    private String nome;
    private LocalDateTime dataHora;
    private String acao;
}
