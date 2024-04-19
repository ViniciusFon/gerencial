package com.gpa.tributario.gerencial.application.usuario.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioSenhaRequest {

    private String passwordOld;
    private String passwordNew;
}
