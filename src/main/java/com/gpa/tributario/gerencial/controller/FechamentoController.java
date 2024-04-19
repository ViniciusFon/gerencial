package com.gpa.tributario.gerencial.controller;

import com.gpa.tributario.gerencial.application.fechamento.FechamentoService;
import com.gpa.tributario.gerencial.application.fechamento.request.FechamentoRequest;
import com.gpa.tributario.gerencial.application.fechamento.response.FechamentoResponse;
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
@RequestMapping("/fechamento")
@Validated
@SecurityRequirement(name = "bearer-schema")
public class FechamentoController {

    @Autowired
    private FechamentoService fechamentoService;

    @GetMapping("/{id}")
    @Operation(summary = "Endpoint que busca o fechamento com o ID informado")
    public FechamentoResponse buscaPorID(@PathVariable("id") String id)  {

        return fechamentoService.buscarPorID(id);
    }

    @GetMapping("/{mes}/{ano}")
    @Operation(summary = "Endpoint que busca o fechamento com a data informada")
    public List<FechamentoResponse> buscarPorData(@PathVariable("mes") @Min(1) @Max(12) Integer mes,
                                                  @PathVariable("ano") @Min(2000) @Max(2099) Integer ano){

        return fechamentoService.buscarPorData(LocalDate.of(ano, mes, 1));
    }

    @GetMapping("/{mes}/{ano}/{uf}")
    @Operation(summary = "Endpoint que busca o fechamento com a data e UF informados")
    public List<FechamentoResponse> buscarPorDataUf(@PathVariable("mes") @Min(1) @Max(12) Integer mes,
                                                    @PathVariable("ano") @Min(2000) @Max(2099) Integer ano,
                                                    @PathVariable("uf") @Size(min = 2, max = 2) String uf){

        return fechamentoService.buscarPorDataUf(LocalDate.of(ano, mes, 1), uf);
    }

    @GetMapping("/{mes}/{ano}/{uf}/{lancamento}")
    @Operation(summary = "Endpoint que busca o fechamento com a data, o idLancamento e UF informados")
    public List<FechamentoResponse> buscarPorDataUfLancamento(@PathVariable("mes") @Min(1) @Max(12) Integer mes,
                                                        @PathVariable("ano") @Min(2000) @Max(2099) Integer ano,
                                                        @PathVariable("uf") @Size(min = 2, max = 2) String uf,
                                                        @PathVariable("lancamento") String lancamento){

        return fechamentoService.buscarPorDataUfLancamento(LocalDate.of(ano, mes, 1), uf, lancamento);
    }

    @GetMapping("/{mes}/{ano}/{uf}/{lancamento}/{tipo}")
    @Operation(summary = "Endpoint que busca o fechamento com a data, o idLancamento e UF informados")
    public List<FechamentoResponse> buscarPorDataUfLancamentoTipo(@PathVariable("mes") @Min(1) @Max(12) Integer mes,
                                                        @PathVariable("ano") @Min(2000) @Max(2099) Integer ano,
                                                        @PathVariable("uf") @Size(min = 2, max = 2) String uf,
                                                        @PathVariable("lancamento") String lancamento,
                                                        @PathVariable("tipo") String tipo){

        return fechamentoService.buscarPorDataUfLancamentoTipo(LocalDate.of(ano, mes, 1), uf, lancamento, tipo);
    }

    @GetMapping("/{mes}/{ano}/{uf}/tipo/{tipo}")
    @Operation(summary = "Endpoint que busca o fechamento com a data, o idLancamento e UF informados")
    public List<FechamentoResponse> buscarPorDataUfTipo(@PathVariable("mes") @Min(1) @Max(12) Integer mes,
                                                                @PathVariable("ano") @Min(2000) @Max(2099) Integer ano,
                                                                @PathVariable("uf") @Size(min = 2, max = 2) String uf,
                                                                @PathVariable("tipo") String tipo){

        return fechamentoService.buscarPorDataUfTipo(LocalDate.of(ano, mes, 1), uf, tipo);
    }

    @GetMapping("/idLancamento/{idLancamento}")
    @Operation(summary = "Endpoint que busca o fechamento com o idLancamento informado")
    public List<FechamentoResponse> buscarPorIdLancamento(@PathVariable("idLancamento") Integer idLancamento){

        return fechamentoService.buscarPorIdLancamento(idLancamento);
    }

    @GetMapping("/grafico/{id}")
    @Operation(summary = "Endpoint que busca os fechamentos do último ano com o id informado")
    public List<FechamentoResponse> buscarGrafico(@PathVariable("id") String id){

        return fechamentoService.buscaGraficoComparacao(id);
    }

    @GetMapping("/tipo/{mes}/{ano}/{uf}/{lancamento}")
    @Operation(summary = "Endpoint que busca todos os tipos dos fechamentos ")
    public List<FechamentoResponse> buscarTiposPorLancamento(@PathVariable("mes") @Min(1) @Max(12) Integer mes,
                                                @PathVariable("ano") @Min(2000) @Max(2099) Integer ano,
                                                @PathVariable("uf") @Size(min = 2, max = 2) String uf,
                                                @PathVariable("lancamento") String lancamento){

        return fechamentoService.buscaTodosEventosPorLancamento(LocalDate.of(ano, mes, 1), uf, lancamento);
    }

    @GetMapping("/tipo/{mes}/{ano}/{uf}")
    @Operation(summary = "Endpoint que busca todos os tipos dos fechamentos ")
    public List<FechamentoResponse> buscarTiposPorUf(@PathVariable("mes") @Min(1) @Max(12) Integer mes,
                                                             @PathVariable("ano") @Min(2000) @Max(2099) Integer ano,
                                                             @PathVariable("uf") @Size(min = 2, max = 2) String uf){

        return fechamentoService.buscaTodosEventosPorUf(LocalDate.of(ano, mes, 1), uf);
    }

    @GetMapping("/lancamento/{mes}/{ano}/{uf}")
    @Operation(summary = "Endpoint que busca todos os lancamentos dos fechamentos ")
    public List<FechamentoResponse> buscarLancamentos(@PathVariable("mes") @Min(1) @Max(12) Integer mes,
                                                       @PathVariable("ano") @Min(2000) @Max(2099) Integer ano,
                                                       @PathVariable("uf") @Size(min = 2, max = 2) String uf){

        return fechamentoService.buscaTodosLancamentos(LocalDate.of(ano, mes, 1), uf);
    }

    @PostMapping("/importAll")
    @Operation(summary = "Endpoint que importa todo o arquivo de fechamento para o banco")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public void importAll(){
        fechamentoService.importAll();
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Endpoint que importa um novo arquivo de fechamento para o banco")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    public void upload(@RequestPart("file") MultipartFile file){
        fechamentoService.incrementaDados(file);
    }

    @PostMapping("/")
    @Operation(summary = "Endpoint que insere um novo fechamento")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SUPER', 'GERENCIA')")
    public FechamentoResponse inserir(@RequestBody @Valid FechamentoRequest request){
        return fechamentoService.inserir(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Endpoint que altera o fechamento com o id informado")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SUPER', 'GERENCIA')")
    public FechamentoResponse alterar(@PathVariable("id") String id,
                                      @RequestBody @Valid FechamentoRequest request){

        return fechamentoService.alterar(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Endpoint que remove o fechamento com o id informado")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'SUPER', 'GERENCIA')")
    public void deletar(@PathVariable("id") String id){
        fechamentoService.remover(id);
    }

}
