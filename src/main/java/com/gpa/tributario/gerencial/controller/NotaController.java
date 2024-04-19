package com.gpa.tributario.gerencial.controller;

import com.gpa.tributario.gerencial.application.nota.NotaService;
import com.gpa.tributario.gerencial.application.nota.request.NotaRequest;
import com.gpa.tributario.gerencial.application.nota.response.NotaResponse;
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
@RequestMapping("/nota")
@Validated
@SecurityRequirement(name = "bearer-schema")
public class NotaController {

    @Autowired
    private NotaService notaService;

    @GetMapping("/{id}")
    @Operation(summary = "Endpoint que busca a Nota com o ID informado")
    public NotaResponse buscaPorID(@PathVariable("id") String id){
        return notaService.buscaPorID(id);
    }

    @GetMapping("/{mes}/{ano}")
    @Operation(summary = "Endpoint que busca as Notas com a data informada")
    public List<NotaResponse> buscaPorData(@PathVariable("mes") @Min(1) @Max(12) Integer mes,
                                                @PathVariable("ano") @Min(2000) @Max(2099) Integer ano){

        return notaService.buscaPorData(LocalDate.of(ano, mes, 1));
    }

    @GetMapping("/{mes}/{ano}/{uf}")
    @Operation(summary = "Endpoint que busca as Notas com a data e o estado informados")
    public List<NotaResponse> buscaPorDataUf(@PathVariable("mes") @Min(1) @Max(12) Integer mes,
                                                    @PathVariable("ano") @Min(2000) @Max(2099) Integer ano,
                                                    @PathVariable("uf") @Size(min = 2, max = 2) String uf){

        return notaService.buscaPorDataUf(LocalDate.of(ano, mes, 1), uf);
    }

    @GetMapping("/{mes}/{ano}/{uf}/{empresa}")
    @Operation(summary = "Endpoint que busca as Notas com a data, o estado e a empresa informados")
    public List<NotaResponse> buscaPorDataEmpresaUf(@PathVariable("mes") @Min(1) @Max(12) Integer mes,
                                                    @PathVariable("ano") @Min(2000) @Max(2099) Integer ano,
                                                    @PathVariable("uf") @Size(min = 2, max = 2) String uf,
                                                    @PathVariable("empresa") EmpresaEnum empresa){

        return notaService.buscaPorDataEmpresaUf(LocalDate.of(ano, mes, 1), empresa, uf);
    }

    @GetMapping("/{mes}/{ano}/{uf}/{empresa}/{cfop}")
    @Operation(summary = "Endpoint que busca as Notas com a data, o estado, a empresa e o Cfop informados")
    public List<NotaResponse> buscaPorDataEmpresaUfCfop(@PathVariable("mes") @Min(1) @Max(12) Integer mes,
                                                        @PathVariable("ano") @Min(2000) @Max(2099) Integer ano,
                                                        @PathVariable("uf") @Size(min = 2, max = 2) String uf,
                                                        @PathVariable("empresa") EmpresaEnum empresa,
                                                        @PathVariable("cfop") Integer cfop){

        return notaService.buscaPorDataEmpresaUfCfop(LocalDate.of(ano, mes, 1), empresa, uf, cfop);
    }

    @PostMapping("/")
    @Operation(summary = "Endpoint que insere uma nova Notas")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SUPER', 'GERENCIA')")
    public NotaResponse inserir(@RequestBody @Valid NotaRequest request){
        return notaService.inserir(request);
    }

    @PostMapping("/importAll")
    @Operation(summary = "Endpoint que importa todo o arquivo de NOTAS para o banco")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public void importAll(){
        notaService.importAll();
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Endpoint que importa um novo arquivo de Nota para o banco")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public void upload(@RequestPart("file") MultipartFile file){
        notaService.incrementaDados(file);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Endpoint que altera a Nota com o ID informado")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SUPER', 'GERENCIA')")
    public NotaResponse alterar(@PathVariable("id") String id, @RequestBody @Valid NotaRequest request){
        return notaService.alterar(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Endpoint que remove a Nota com o ID informado")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SUPER', 'GERENCIA')")
    public void remover(@PathVariable("id") String id){
        notaService.remover(id);
    }
}
