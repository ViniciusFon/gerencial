package com.gpa.tributario.gerencial.entity;

import com.gpa.tributario.gerencial.enuns.RoleNameEnum;
import com.gpa.tributario.gerencial.enuns.TipoAcaoUsuarioEnum;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "usuario")
public class Usuario {

    @Id
    private String userName;

    private String nome;

    private String password;

    private String email;

    private boolean ativo = true;

    private List<RoleNameEnum> roles;

    private List<HistoricoUsuario> historicoAlteracao;

    public void addHistorico(String usuario, TipoAcaoUsuarioEnum tipoAcao){
        if(historicoAlteracao == null){
            historicoAlteracao = new ArrayList<>();
        }

        historicoAlteracao.add(new HistoricoUsuario(LocalDateTime.now(), usuario, tipoAcao));
    }
}
