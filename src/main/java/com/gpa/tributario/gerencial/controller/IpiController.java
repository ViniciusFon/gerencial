package com.gpa.tributario.gerencial.controller;

import com.gpa.tributario.gerencial.application.ipi.IpiService;
import com.gpa.tributario.gerencial.application.ipi.request.IpiRequest;
import com.gpa.tributario.gerencial.application.ipi.response.IpiResponse;
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
@RequestMapping("/ipi")
@Validated
@SecurityRequirement(name = "bearer-schema")
public class IpiController {

    @Autowired
    private IpiService ipiService;

    @GetMapping("/{id}")
    @Operation(summary = "Endpoint que busca o IPI com o ID informado")
    public IpiResponse buscaPorID(@PathVariable("id") String id){

        return ipiService.buscaPorID(id);
    }

    @GetMapping("/{mes}/{ano}")
    @Operation(summary = "Endpoint que busca o IPI com a data informada")
    public List<IpiResponse> buscaPoUfLoja(@PathVariable("mes") @Min(1) @Max(12) Integer mes,
                                           @PathVariable("ano") @Min(2000) @Max(2099) Integer ano){

        return ipiService.buscaPorData(LocalDate.of(ano, mes, 1));
    }

    @GetMapping("/{mes}/{ano}/{uf}")
    @Operation(summary = "Endpoint que busca o IPI com a data e estado informados")
    public List<IpiResponse> buscaPorDataLoja(@PathVariable("mes") @Min(1) @Max(12) Integer mes,
                                              @PathVariable("ano") @Min(2000) @Max(2099) Integer ano,
                                              @PathVariable("uf") @Size(min = 2, max = 2) String uf){

        return ipiService.buscaPorDataUf(LocalDate.of(ano, mes, 1), uf);
    }

    @GetMapping("/{mes}/{ano}/{uf}/{loja}")
    @Operation(summary = "Endpoint que busca o IPI com a data, o estado e Loja informados")
    public List<IpiResponse> buscaPorDataUfLoja(@PathVariable("mes") @Min(1) @Max(12) Integer mes,
                                                @PathVariable("ano") @Min(2000) @Max(2099) Integer ano,
                                                @PathVariable("uf") @Size(min = 2, max = 2) String uf,
                                                @PathVariable("loja") Integer loja){

        return ipiService.buscaPorDataUfLoja(LocalDate.of(ano, mes, 1), uf, loja);
    }


    @PostMapping("/")
    @Operation(summary = "Endpoint que insere um novo IPI")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SUPER', 'GERENCIA')")
    public IpiResponse inserir(@RequestBody @Valid IpiRequest request){
        return ipiService.inserir(request);
    }

    @PostMapping("/importAll")
    @Operation(summary = "Endpoint que importa todo o arquivo de IPI para o banco")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public void importAll(){
        ipiService.importAll();
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Endpoint que importa um novo arquivo de Ipi para o banco")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public void upload(@RequestPart("file") MultipartFile file){
        ipiService.incrementaDados(file);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Endpoint que altera o IPI com o ID informado")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SUPER', 'GERENCIA')")
    public IpiResponse alterar(@PathVariable("id") String id, @RequestBody @Valid IpiRequest request){
        return ipiService.alterar(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Endpoint que remove o IPI com o ID informado")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SUPER', 'GERENCIA')")
    public void remover(@PathVariable("id") String id){
        ipiService.remover(id);
    }
}
