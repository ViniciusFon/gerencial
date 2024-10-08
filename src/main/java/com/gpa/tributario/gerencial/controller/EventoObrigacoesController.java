package com.gpa.tributario.gerencial.controller;

import com.gpa.tributario.gerencial.application.eventoObrigacoes.EventoObrigacoesService;
import com.gpa.tributario.gerencial.application.eventoObrigacoes.request.EventoObrigacoesRequest;
import com.gpa.tributario.gerencial.application.eventoObrigacoes.response.EventoObrigacoesResponse;
import com.gpa.tributario.gerencial.application.eventoObrigacoes.response.EventoObrigacoesTotaisResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/eventoObrigacoes")
@Validated
@SecurityRequirement(name = "bearer-schema")
public class EventoObrigacoesController {

    @Autowired
    private EventoObrigacoesService service;


    @GetMapping("/")
    @Operation(summary = "Endpoint que busca todos eventos de obrigações")
    public List<EventoObrigacoesResponse> buscaTodos(){
        return service.buscaTodos();
    }

    @GetMapping("/UF/{uf}")
    @Operation(summary = "Endpoint que busca o eventos de obrigações coma UF informada")
    public List<EventoObrigacoesResponse> buscaPorUF(@PathVariable("uf") @Size(min = 2, max = 2) String uf){
        return service.buscaPorUF(uf);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Endpoint que busca o relatório obrigações com o ID informado")
    public EventoObrigacoesResponse buscaPorID(@PathVariable("id") String id){
        return service.buscaPorID(id);
    }

    @GetMapping("/eventos/totais")
    @Operation(summary = "Endpoint que busca os totais das obrigações agrupados por UF")
    public List<EventoObrigacoesTotaisResponse> buscaTotais(){
        return service.buscaTotais();
    }

    @GetMapping("/eventos/totais/obrigacoes")
    @Operation(summary = "Endpoint que busca os totais das obrigações agrupados por tipo")
    public List<EventoObrigacoesTotaisResponse> buscaTotaisObrig(){
        return service.buscaTotaisObrig();
    }

    @PostMapping("/")
    @Operation(summary = "Endpoint que insere um relatório de obrigações")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SUPER', 'GERENCIA')")
    public void inserir(@RequestBody @Valid EventoObrigacoesRequest request){
        service.insert(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Endpoint que altera o relatório de obrigações com o ID informado")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SUPER', 'GERENCIA')")
    public EventoObrigacoesResponse alterar(@PathVariable("id") String id, @RequestBody @Valid EventoObrigacoesRequest request){
        return service.alterar(id, request);
    }

    @PutMapping("/upload/arquivos/sped")
    @Operation(summary = "Endpoint que marca as obrigações dos arquivos recebidos")
    public void recebeSped( @RequestBody List<String> arquivos){
        service.recebeSped(arquivos);
    }

    @PostMapping(value = "/upload/arquivo/gia", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Endpoint que marca as obrigações do arquivo recebido")
    public void recebeGia(@RequestPart("file") MultipartFile file){
        service.recebeGia(file);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Endpoint que altera o status do relatório de obrigações com o ID informado")
    public void alteraStatus(@PathVariable("id") String id, @RequestBody boolean concluido){
        service.alteraStatus(id, concluido);
    }

    @PatchMapping("/{id}/{nome}")
    @Operation(summary = "Endpoint que altera o nome do relatório de obrigações com o ID informado")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SUPER', 'GERENCIA')")
    public void alteraNome(@PathVariable("id") String id, @PathVariable("nome") String nome){
        service.alteraNome(id, nome);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Endpoint que apaga o relatório de obrigações com o ID informado")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SUPER', 'GERENCIA')")
    public void delete(@PathVariable("id") String id){
        service.delete(id);
    }

    @PutMapping("/ZERAR/TOTAL")
    @Operation(summary = "Endpoint que zera o status e limpa o hitórico de todos os eventos de obrigações")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public void zerarTudo(){
        service.zerarTudo();
    }

    @PutMapping("/ZERAR")
    @Operation(summary = "Endpoint que zera o status de todos os eventos de obrigações")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SUPER', 'GERENCIA')")
    public void zerar(){
        service.zerar();
    }

    @PostMapping("/importAll")
    @Operation(summary = "Endpoint que importa todo o arquivo de faturamento para o banco")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public void importAll(){
        service.importAll();
    }

}
