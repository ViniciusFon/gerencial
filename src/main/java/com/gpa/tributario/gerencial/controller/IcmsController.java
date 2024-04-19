package com.gpa.tributario.gerencial.controller;

import com.gpa.tributario.gerencial.application.icms.IcmsService;
import com.gpa.tributario.gerencial.application.icms.request.IcmsRequest;
import com.gpa.tributario.gerencial.application.icms.response.IcmsResponse;
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
@RequestMapping("/icms")
@Validated
@SecurityRequirement(name = "bearer-schema")
public class IcmsController {

    @Autowired
    private IcmsService icmsService;


    @GetMapping("/{id}")
    @Operation(summary = "Endpoint que busca o ICMS com o ID informado")
    public IcmsResponse buscarPorID(@PathVariable("id") String id){
        return icmsService.buscaPorID(id);
    }

    @GetMapping("/{mes}/{ano}")
    @Operation(summary = "Endpoint que busca o ICMS com a data informada")
    public List<IcmsResponse> buscarPorEstadoCfop(@PathVariable("mes") @Min(1) @Max(12) Integer mes,
                                                  @PathVariable("ano") @Min(2000) @Max(2099) Integer ano){

        return icmsService.buscarPorData(LocalDate.of(ano, mes, 1));
    }

    @GetMapping("/{mes}/{ano}/{uf}")
    @Operation(summary = "Endpoint que busca o ICMS com a data e o estado informados")
    public List<IcmsResponse> buscarPorDataEstado(@PathVariable("mes") @Min(1) @Max(12) Integer mes,
                                                  @PathVariable("ano") @Min(2000) @Max(2099) Integer ano,
                                                  @PathVariable("uf") @Size(min = 2, max = 2) String uf){

        return icmsService.buscarPorDataEstado(LocalDate.of(ano, mes, 1), uf);
    }

    @GetMapping("/{mes}/{ano}/{uf}/{cfop}")
    @Operation(summary = "Endpoint que busca o ICMS com a data, o estado e o CFOP informados")
    public List<IcmsResponse> buscarPorDataEstadoCfop(@PathVariable("mes") @Min(1) @Max(12) Integer mes,
                                                      @PathVariable("ano") @Min(2000) @Max(2099) Integer ano,
                                                      @PathVariable("uf") @Size(min = 2, max = 2) String uf,
                                                      @PathVariable("cfop") Integer cfop){

        return icmsService.buscarPorDataEstadoCfop(LocalDate.of(ano, mes, 1), uf, cfop);
    }


    @PostMapping("/")
    @Operation(summary = "Endpoint que insere um novo ICMS")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SUPER', 'GERENCIA')")
    public IcmsResponse inserir(@RequestBody @Valid IcmsRequest request){
        return icmsService.inserir(request);
    }

    @PostMapping("/importAll")
    @Operation(summary = "Endpoint que importa todo o arquivo de ICMS para o banco")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public void importAll(){
        icmsService.importAll();
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Endpoint que importa um novo arquivo de Icms para o banco")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public void upload(@RequestPart("file") MultipartFile file){
        icmsService.incrementaDados(file);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Endpoint que alterar o ICMS com o ID informado")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SUPER', 'GERENCIA')")
    public IcmsResponse alterar(@PathVariable("id") String id, @RequestBody @Valid IcmsRequest request){
        return icmsService.alterar(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Endpoint que remove o ICMS com o ID informado")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SUPER', 'GERENCIA')")
    public void remover(@PathVariable("id") String id){
        icmsService.remover(id);
    }
}
