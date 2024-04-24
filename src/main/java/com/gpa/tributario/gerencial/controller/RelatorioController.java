package com.gpa.tributario.gerencial.controller;

import com.gpa.tributario.gerencial.application.relatorio.RelatorioService;
import com.gpa.tributario.gerencial.application.relatorio.request.RelatorioRequest;
import com.gpa.tributario.gerencial.application.relatorio.response.RelatorioResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/relatorio")
@Validated
@SecurityRequirement(name = "bearer-schema")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/{mes}/{ano}/{uf}")
    public RelatorioResponse buscaPorDataUf(@PathVariable("mes") @Min(1) @Max(12) Integer mes,
                                            @PathVariable("ano") @Min(2000) @Max(2099) Integer ano,
                                            @PathVariable("uf") @Size(min = 2, max = 2) String uf){
        return relatorioService.buscarPorDataUF(LocalDate.of(ano, mes, 1), uf);
    }

    @GetMapping("/{mes}/{ano}")
    public List<RelatorioResponse> buscaPorData(@PathVariable("mes") @Min(1) @Max(12) Integer mes, @PathVariable("ano") @Min(2000) @Max(2099) Integer ano){
        return relatorioService.buscaPorData(LocalDate.of(ano, mes, 1));
    }

    @PostMapping("/processar/{mes}/{ano}")
    public void processar(@PathVariable("mes") @Min(1) @Max(12) Integer mes, @PathVariable("ano") @Min(2000) @Max(2099) Integer ano){

        relatorioService.processaRelatorioPorData(LocalDate.of(ano, mes, 1));
    }

    @PatchMapping("/{mes}/{ano}/{uf}")
    public RelatorioResponse alterar(@PathVariable("mes") @Min(1) @Max(12) Integer mes,
                                     @PathVariable("ano") @Min(2000) @Max(2099) Integer ano,
                                     @PathVariable("uf") @Size(min = 2, max = 2) String uf,
                                     @RequestBody RelatorioRequest request){

        return relatorioService.alterar(LocalDate.of(ano, mes, 1), uf, request);
    }
}
