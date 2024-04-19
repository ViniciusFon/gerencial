package com.gpa.tributario.gerencial.controller;

import com.gpa.tributario.gerencial.application.usuario.UsuarioService;
import com.gpa.tributario.gerencial.application.usuario.request.LoginUsuarioRequest;
import com.gpa.tributario.gerencial.application.usuario.request.UsuarioRequest;
import com.gpa.tributario.gerencial.application.usuario.request.UsuarioSenhaRequest;
import com.gpa.tributario.gerencial.application.usuario.response.UsuarioResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/usuario")
@Validated
@SecurityRequirement(name = "bearer-schema")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/")
    @Operation(summary = "Endpoint que busca todos usuários")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','SUPER','GERENCIA')")
    public List<UsuarioResponse> buscarTodos(){
        return usuarioService.buscarTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Endpoint que busca o usuário com o Id informado")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR','SUPER','GERENCIA)")
    public UsuarioResponse buscarPorId(@PathVariable("id") String id){
        return usuarioService.buscaPorId(id);
    }

    @PostMapping("/")
    @Operation(summary = "Endpoint que cadastra um usuário")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public void inserir(@RequestBody @Valid UsuarioRequest request){
         usuarioService.createUser(request);
    }

    @PutMapping("/alterar/senha")
    @Operation(summary = "Endpoint que altera a senha do usuário logado")
    public void alterarSenha(@RequestBody @Valid UsuarioSenhaRequest request){
        usuarioService.alterarSenha(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Endpoint que altera o usuário com o Id informado")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public void alterar(@PathVariable("id") String id, @RequestBody @Valid UsuarioRequest request){
        usuarioService.alterar(id, request);
    }

    @PostMapping("/login")
    @Operation(summary = "Endpoint que faz a autenticação do usuário")
    public ResponseEntity<UsuarioResponse> authenticateUser(@RequestBody @Valid LoginUsuarioRequest request) {
        UsuarioResponse usuarioResponse = usuarioService.authenticateUser(request);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + usuarioResponse.getToken());

        // Retorne a resposta com os dados e o cabeçalho
        return ResponseEntity.ok()
                .headers(headers)
                .body(usuarioResponse);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Endpoint que remove o usuário com o ID informado")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public void remover(@PathVariable("id") String id){
        usuarioService.remover(id);
    }

}
