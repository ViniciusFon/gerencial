package com.gpa.tributario.gerencial.controller;

import com.gpa.tributario.gerencial.application.faturamento.FaturamentoService;
import com.gpa.tributario.gerencial.application.faturamento.request.FaturamentoRequest;
import com.gpa.tributario.gerencial.application.faturamento.response.FaturamentoResponse;
import com.gpa.tributario.gerencial.application.faturamento.response.GraficoFaturamentoResponse;
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
@RequestMapping("/faturamento")
@Validated
@SecurityRequirement(name = "bearer-schema")
public class FaturamentoController {

    @Autowired
    private FaturamentoService faturamentoService;

    @GetMapping("/{id}")
    @Operation(summary = "Endpoint que busca o faturamento com o ID informado")
    public FaturamentoResponse buscaPorID(@PathVariable("id") String id)  {

        return faturamentoService.buscaPorID(id);
    }

    @GetMapping("/{mes}/{ano}")
    @Operation(summary = "Endpoint que busca o faturamento com a data informada")
    public List<FaturamentoResponse> buscarPorData(@PathVariable("mes") @Min(1) @Max(12) Integer mes,
                                                   @PathVariable("ano") @Min(2000) @Max(2099) Integer ano){

        return faturamentoService.buscaPorData(LocalDate.of(ano, mes, 1));
    }

    @GetMapping("/{mes}/{ano}/{uf}")
    @Operation(summary = "Endpoint que busca o faturamento com a data e UF informados")
    public List<FaturamentoResponse> buscarPorDataUf(@PathVariable("mes") @Min(1) @Max(12) Integer mes,
                                                     @PathVariable("ano") @Min(2000) @Max(2099) Integer ano,
                                                     @PathVariable("uf") @Size(min = 2, max = 2) String uf){

        return faturamentoService.buscaPorDataUf(LocalDate.of(ano, mes, 1), uf);
    }

    @GetMapping("/UF/{uf}")
    @Operation(summary = "Endpoint que busca o faturamento com a UF informada")
    public List<FaturamentoResponse> buscarPorUf(@PathVariable("uf") @Size(min = 2, max = 2) String uf){

        return faturamentoService.buscaPorUf(uf);
    }

    @GetMapping("/grafico/{mes}/{ano}")
    @Operation(summary = "Endpoint que busca o faturamento com a UF informada")
    public List<GraficoFaturamentoResponse> buscarPorUf(@PathVariable("mes") @Min(1) @Max(12) Integer mes,
                                                        @PathVariable("ano") @Min(2000) @Max(2099) Integer ano){

        return faturamentoService.buscaGraficoFaturamento(LocalDate.of(ano, mes, 1));
    }

    @PostMapping("/importAll")
    @Operation(summary = "Endpoint que importa todo o arquivo de faturamento para o banco")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public void importAll(){
        faturamentoService.importAll();
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Endpoint que importa um novo arquivo de Faturamento para o banco")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public void upload(@RequestPart("file") MultipartFile file){
        faturamentoService.incrementaDados(file);
    }

    @PostMapping("/")
    @Operation(summary = "Endpoint que insere um novo faturamento")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SUPER', 'GERENCIA')")
    public FaturamentoResponse inserir(@RequestBody @Valid FaturamentoRequest request){
        return faturamentoService.inserir(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Endpoint que altera o faturamento com o id informado")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SUPER', 'GERENCIA')")
    public FaturamentoResponse alterar(@PathVariable("id") String id,
                                      @RequestBody @Valid FaturamentoRequest request){

        return faturamentoService.alterar(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Endpoint que remove o faturamento com o id informado")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SUPER', 'GERENCIA')")
    public void deletar(@PathVariable("id") String id){

        faturamentoService.remover(id);
    }


}
