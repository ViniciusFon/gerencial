package com.gpa.tributario.gerencial.controller;

import com.gpa.tributario.gerencial.application.apuracao.ApuracaoService;
import com.gpa.tributario.gerencial.application.apuracao.request.ApuracaoRequest;
import com.gpa.tributario.gerencial.application.apuracao.response.ApuracaoResponse;
import com.gpa.tributario.gerencial.enuns.EmpresaEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/apuracao")
@Validated
@SecurityRequirement(name = "bearer-schema")
public class ApuracaoController {

    @Autowired
    private ApuracaoService apuracaoService;

    @GetMapping("/{id}")
    @Operation(summary = "Endpoint que busca a Operação com o ID informado")
    public ApuracaoResponse buscaPorID( @PathVariable("id") String id){
        return apuracaoService.buscaPorID(id);
    }

    @GetMapping("/{mes}/{ano}")
    @Operation(summary = "Endpoint que busca as Operações com data informada")
    public List<ApuracaoResponse> buscarPorData(@PathVariable("mes") @Min(1) @Max(12) Integer mes,
                                                @PathVariable("ano") @Min(2000) @Max(2099) Integer ano){

        return apuracaoService.buscarPorData(LocalDate.of(ano, mes, 1));
    }

    @GetMapping("/{mes}/{ano}/{uf}")
    @Operation(summary = "Endpoint que busca as Operações com data e o estado informados")
    public List<ApuracaoResponse> buscarPorDataEmpresa(@PathVariable("mes") @Min(1) @Max(12) Integer mes,
                                                       @PathVariable("ano") @Min(2000) @Max(2099) Integer ano,
                                                       @PathVariable("uf") @Size(min = 2, max = 2) String uf){

        return apuracaoService.buscarPorDataUf(LocalDate.of(ano, mes, 1), uf);
    }

    @GetMapping("/{mes}/{ano}/{uf}/{empresa}")
    @Operation(summary = "Endpoint que busca as Operações com data, a empresa e UF informados")
    public List<ApuracaoResponse> buscarPorDataEmpresaUf(@PathVariable("mes") @Min(1) @Max(12) Integer mes,
                                                         @PathVariable("ano") @Min(2000) @Max(2099) Integer ano,
                                                         @PathVariable("uf") @Size(min = 2, max = 2) String uf,
                                                         @PathVariable("empresa") EmpresaEnum empresa){

        return apuracaoService.buscarPorDataEmpresaUf(LocalDate.of(ano, mes, 1), empresa, uf);
    }

    @PostMapping("/importAll")
    @Operation(summary = "Endpoint que importa todo o arquivo de apuração para o banco")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public void importAll(){
        apuracaoService.importAll();
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Endpoint que importa um novo arquivo de Apuração para o banco")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public void upload(@RequestPart("file") MultipartFile file){
        apuracaoService.incrementaDados(file);
    }

    @PostMapping("/")
    @Operation(summary = "Endpoint que insere uma Operação")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SUPER', 'GERENCIA')")
    public ApuracaoResponse inserir(@RequestBody @Valid ApuracaoRequest request){
        return apuracaoService.inserir(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Endpoint que altera a Operação com o ID informado")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SUPER', 'GERENCIA')")
    public ApuracaoResponse alterar(@PathVariable("id") String id, @RequestBody @Valid ApuracaoRequest request){
        return apuracaoService.alterar(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Endpoint que remove a Operação com o ID informado")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SUPER', 'GERENCIA')")
    public void deletar(@PathVariable("id") String id){
        apuracaoService.remover(id);
    }
}
