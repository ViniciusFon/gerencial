package com.gpa.tributario.gerencial.controller;

import com.gpa.tributario.gerencial.application.monetizacao.MonetizacaoService;
import com.gpa.tributario.gerencial.application.monetizacao.request.MonetizacaoRequest;
import com.gpa.tributario.gerencial.application.monetizacao.response.MonetizacaoResponse;
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
@RequestMapping("/monetizacao")
@Validated
@SecurityRequirement(name = "bearer-schema")
public class MonetizacaoController {

    @Autowired
    private MonetizacaoService monetizacaoService;

    @GetMapping("/{id}")
    @Operation(summary = "Endpoint que busca a Monetização com o ID informado")
    public MonetizacaoResponse buscaPorID(@PathVariable("id") String id){
        return monetizacaoService.buscaPorID(id);
    }

    @GetMapping("/{mes}/{ano}")
    @Operation(summary = "Endpoint que busca as Monetizações com a data informada")
    public List<MonetizacaoResponse> buscaPorData(@PathVariable("mes") @Min(1) @Max(12) Integer mes,
                                                    @PathVariable("ano") @Min(2000) @Max(2099) Integer ano){

        return monetizacaoService.buscaPorData(LocalDate.of(ano, mes, 1));
    }

    @GetMapping("/{mes}/{ano}/{uf}")
    @Operation(summary = "Endpoint que busca as Monetizações com a data e o estado informados")
    public List<MonetizacaoResponse> buscaPorDataUf(@PathVariable("mes") @Min(1) @Max(12) Integer mes,
                                                    @PathVariable("ano") @Min(2000) @Max(2099) Integer ano,
                                                    @PathVariable("uf") @Size(min = 2, max = 2) String uf){

        return monetizacaoService.buscaPorDataUf(LocalDate.of(ano, mes, 1), uf);
    }


    @PostMapping("/")
    @Operation(summary = "Endpoint que insere uma nova a Monetização")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SUPER', 'GERENCIA')")
    public MonetizacaoResponse inserir(@RequestBody @Valid MonetizacaoRequest request){
        return monetizacaoService.inserir(request);
    }


    @PostMapping("/importAll")
    @Operation(summary = "Endpoint que importa todo o arquivo de Monetização para o banco")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public void importAll(){
        monetizacaoService.importAll();
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Endpoint que importa um novo arquivo de Monetização para o banco")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public void upload(@RequestPart("file") MultipartFile file){
        monetizacaoService.incrementaDados(file);
    }


    @PutMapping("/{id}")
    @Operation(summary = "Endpoint que alterar a Monetização com o ID informado")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SUPER', 'GERENCIA')")
    public MonetizacaoResponse alterar(@PathVariable("id") String id,
                                       @RequestBody @Valid MonetizacaoRequest request){

        return monetizacaoService.alterar(id, request);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Endpoint que remove a Monetização com o ID informado")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SUPER', 'GERENCIA')")
    public void remover(@PathVariable("id") String id){
        monetizacaoService.remover(id);
    }

}
