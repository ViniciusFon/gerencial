package com.gpa.tributario.gerencial.entity;

import com.gpa.tributario.gerencial.enuns.TipoAcaoUsuarioEnum;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoUsuario {

    private LocalDateTime dataAlteracao;
    private String usuarioAlteracao;
    private TipoAcaoUsuarioEnum tipoAcao;
}
