package com.gpa.tributario.gerencial.application.usuario.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gpa.tributario.gerencial.enuns.RoleNameEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UsuarioResponse {

    private String userName;
    private String nome;
    private String email;
    private boolean fazFechamento;
    private List<RoleNameEnum> grupos;

    @JsonIgnore
    private String token;

}
