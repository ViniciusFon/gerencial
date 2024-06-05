package com.gpa.tributario.gerencial.controller;

import com.gpa.tributario.gerencial.application.eventoFechamento.EventoFechamentoService;
import com.gpa.tributario.gerencial.application.eventoFechamento.request.EventoFechamentoRequest;
import com.gpa.tributario.gerencial.application.eventoFechamento.response.EventoFechamentoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/eventoFechamento")
@Validated
@SecurityRequirement(name = "bearer-schema")
public class EventoFechamentoController {

    @Autowired
    private EventoFechamentoService service;

    @GetMapping("/")
    @Operation(summary = "Endpoint que busca todos eventos de fechamento")
    public List<EventoFechamentoResponse> buscaTodos(){
        return service.buscaTodos();
    }

    @GetMapping("/UF/{uf}")
    @Operation(summary = "Endpoint que busca o eventos de fechamento coma UF informada")
    public List<EventoFechamentoResponse> buscaPorUF(@PathVariable("uf") @Size(min = 2, max = 2) String uf){
        return service.buscaPorUF(uf);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Endpoint que busca o relatório fechamento com o ID informado")
    public EventoFechamentoResponse buscaPorID(@PathVariable("id") String id){
        return service.buscaPorID(id);
    }

    @PostMapping("/")
    @Operation(summary = "Endpoint que insere um relatório de fechamento")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SUPER', 'GERENCIA')")
    public void inserir(@RequestBody @Valid EventoFechamentoRequest request){
        service.insert(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Endpoint que altera o relatório de fechamento com o ID informado")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SUPER', 'GERENCIA')")
    public EventoFechamentoResponse alterar(@PathVariable("id") String id, @RequestBody @Valid EventoFechamentoRequest request){
        return service.alterar(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Endpoint que apaga o relatório de fechamento com o ID informado")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SUPER', 'GERENCIA')")
    public void delete(@PathVariable("id") String id){
        service.delete(id);
    }

    @PutMapping("/ZERAR/TOTAL")
    @Operation(summary = "Endpoint que zera o status de todos os eventos de fechamento")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SUPER', 'GERENCIA')")
    public void zerar(){
        service.zerar();
    }
}
