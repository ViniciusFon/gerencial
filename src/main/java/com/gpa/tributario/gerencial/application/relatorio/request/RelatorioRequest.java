package com.gpa.tributario.gerencial.application.relatorio.request;

import com.gpa.tributario.gerencial.enuns.ScoreEnum;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioRequest {

    private boolean regimeEspecial;
    private ScoreEnum scoreFiscalizacao;
}
