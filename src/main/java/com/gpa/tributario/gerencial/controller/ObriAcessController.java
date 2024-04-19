package com.gpa.tributario.gerencial.controller;

import com.gpa.tributario.gerencial.application.obriacess.ObriAcessService;
import com.gpa.tributario.gerencial.application.obriacess.request.ObriAcessRequest;
import com.gpa.tributario.gerencial.application.obriacess.response.ObriAcessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.extern.log4j.Log4j2;
import org.hibernate.validator.constraints.br.CNPJ;
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
@RequestMapping("/obriacess")
@Validated
@SecurityRequirement(name = "bearer-schema")
public class ObriAcessController {

    @Autowired
    private ObriAcessService obriAcessService;

    @GetMapping("/{id}")
    @Operation(summary = "Endpoint que busca a Obrigação de Acessório com o ID informado")
    public ObriAcessResponse buscaPorID(@PathVariable("id") String id){
        return obriAcessService.buscaPorID(id);
    }

    @GetMapping("/{mes}/{ano}/{uf}/{codEstabelecimento}")
    @Operation(summary = "Endpoint que busca a Obrigação de Acessório com a data, o estado e o código do estabelecimento informado")
    public List<ObriAcessResponse> buscaPorDataUfCodEstabelecimento(@PathVariable("mes") @Min(1) @Max(12) Integer mes,
                                                                    @PathVariable("ano") @Min(2000) @Max(2099) Integer ano,
                                                                    @PathVariable("uf") @Size(min = 2, max = 2) String uf,
                                                                    @PathVariable("codEstabelecimento") String codEstabelecimento){
        return obriAcessService.buscaPorDataUfCodEstabelecimento(LocalDate.of(ano, mes, 1), uf, codEstabelecimento);
    }

    @GetMapping("/{mes}/{ano}/{uf}")
    @Operation(summary = "Endpoint que busca a Obrigação de Acessório com a data e o estado informado")
    public List<ObriAcessResponse> buscaPorDataUf(@PathVariable("mes") @Min(1) @Max(12) Integer mes,
                                                  @PathVariable("ano") @Min(2000) @Max(2099) Integer ano,
                                                  @PathVariable("uf") @Size(min = 2, max = 2) String uf){
        return obriAcessService.buscaPorDataUf(LocalDate.of(ano, mes, 1), uf);
    }

    @GetMapping("/{mes}/{ano}/cnpj/{cnpj}")
    @Operation(summary = "Endpoint que busca a Obrigação Acessória com a data e o CNPJ informado")
    public List<ObriAcessResponse> buscaPorDataCnpj(@PathVariable("mes") @Min(1) @Max(12) Integer mes,
                                                    @PathVariable("ano") @Min(2000) @Max(2099) Integer ano,
                                                    @PathVariable("cnpj") @CNPJ String cnpj){
        return obriAcessService.buscaPorDataCnpj(LocalDate.of(ano, mes, 1), cnpj);
    }

    @GetMapping("/cnpj/{cnpj}")
    @Operation(summary = "Endpoint que busca a Obrigação Acessória com o CNPJ informado")
    public List<ObriAcessResponse> buscaPorCnpj(@PathVariable("cnpj") @CNPJ String cnpj){
        return obriAcessService.buscaPorCnpj(cnpj);
    }

    @PostMapping("/")
    @Operation(summary = "Endpoint que insere uma nova Obrigação Acessória")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SUPER', 'GERENCIA')")
    public ObriAcessResponse inserir(@RequestBody @Valid ObriAcessRequest request){
        return obriAcessService.inserir(request);
    }

    @PostMapping("/importAll")
    @Operation(summary = "Endpoint que importa todo o arquivo de Obrigação Acessória para o banco")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public void importAll(){
        obriAcessService.importAll();
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Endpoint que importa um novo arquivo de Obrigações Acessórias para o banco")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public void upload(@RequestPart("file") MultipartFile file){
        obriAcessService.incrementaDados(file);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Endpoint que alterar a Obrigação Acessória com o ID informado")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SUPER', 'GERENCIA')")
    public ObriAcessResponse alterar(@PathVariable("id") String id, @RequestBody @Valid ObriAcessRequest request){
        return obriAcessService.alterar(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Endpoint que remove a Obrigação Acessória com o ID informado")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SUPER', 'GERENCIA')")
    public void remover(@PathVariable("id") String id){
        obriAcessService.remover(id);
    }
}
