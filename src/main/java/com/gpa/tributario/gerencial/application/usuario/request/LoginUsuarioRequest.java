package com.gpa.tributario.gerencial.application.usuario.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUsuarioRequest {

    private String userName;
    private String password;
}
