package com.gpa.tributario.gerencial.application.usuario.request;

import com.gpa.tributario.gerencial.enuns.RoleNameEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UsuarioRequest {

    @NotNull
    @NotBlank
    private String userName;
    @NotNull
    @NotBlank
    private String nome;
    @NotNull
    @NotBlank
    private String password;

    @NotNull
    @Email
    private String email;

    private boolean fazFechamento;

    @NotEmpty
    private List<RoleNameEnum> grupos;

}
