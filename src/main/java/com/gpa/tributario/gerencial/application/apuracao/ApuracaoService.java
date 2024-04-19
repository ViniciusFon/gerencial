package com.gpa.tributario.gerencial.application.apuracao;

import com.gpa.tributario.gerencial.application.ArquivoBaseService;
import com.gpa.tributario.gerencial.application.apuracao.request.ApuracaoRequest;
import com.gpa.tributario.gerencial.application.apuracao.response.ApuracaoResponse;
import com.gpa.tributario.gerencial.application.relatorio.RelatorioService;
import com.gpa.tributario.gerencial.entity.Apuracao;
import com.gpa.tributario.gerencial.enuns.EmpresaEnum;
import com.gpa.tributario.gerencial.infrastructure.exception.NotFoundException;
import com.gpa.tributario.gerencial.repository.ApuracaoRepository;
import com.gpa.tributario.gerencial.utils.CurrencyUtils;
import com.gpa.tributario.gerencial.utils.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ApuracaoService extends ArquivoBaseService<Apuracao> {

    @Value("${arquivo.apuracao.path}")
    private String arquivo;

    @Autowired
    private RelatorioService relatorioService;

    @Autowired
    private ApuracaoRepository apuracaoRepository;

    public ApuracaoResponse buscaPorID(String id) {

        Apuracao apuracao = apuracaoRepository.findById(id).orElseThrow(NotFoundException::new);

        return getResponse(apuracao);
    }

    public void importAll(){
        apuracaoRepository.deleteAll();
        try {
            lerArquivo(new InputStreamReader(new FileInputStream(arquivo), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ApuracaoResponse alterar(String requestID, ApuracaoRequest request) {

        Apuracao apuracao = apuracaoRepository.findById(requestID).orElseThrow(NotFoundException::new);

        BeanUtils.copyProperties(request, apuracao);

        salvar(apuracao);

        relatorioService.processaPorDataUF(apuracao.getData(), apuracao.getUf());

        return getResponse(apuracao);
    }

    public ApuracaoResponse inserir(ApuracaoRequest request)  {

        Apuracao apuracao = new Apuracao();
        BeanUtils.copyProperties(request, apuracao);

        salvar(apuracao);

        relatorioService.processaPorDataUF(apuracao.getData(), apuracao.getUf());

        return getResponse(apuracao);
    }

    public List<ApuracaoResponse> buscarPorDataEmpresaUf(LocalDate data, EmpresaEnum empresa, String uf){

        List<Apuracao> lst = apuracaoRepository.findByDataAndEmpresaAndUf(data, empresa, uf.toUpperCase());

        return lst.stream().map(this::getResponse).toList();
    }

    public List<ApuracaoResponse> buscarPorDataEmpresa(LocalDate data, EmpresaEnum empresa){

        List<Apuracao> lst = apuracaoRepository.findByDataAndEmpresa(data, empresa);

        return lst.stream().map(this::getResponse).toList();
    }

    public List<ApuracaoResponse> buscarPorEmpresaUf(EmpresaEnum empresa, String uf){

        List<Apuracao> lst = apuracaoRepository.findByEmpresaAndUf(empresa, uf.toUpperCase());

        return lst.stream().map(this::getResponse).toList();
    }

    public List<ApuracaoResponse> buscarPorData(LocalDate data){

        List<Apuracao> lst = apuracaoRepository.findByData(data);

        return lst.stream().map(this::getResponse).toList();
    }
    public List<ApuracaoResponse> buscarPorDataUf(LocalDate data, String uf){

        List<Apuracao> lst = apuracaoRepository.findByDataAndUf(data, uf.toUpperCase());

        return lst.stream().map(this::getResponse).toList();
    }

    public void remover(String id){

        Apuracao apuracao = apuracaoRepository.findById(id).orElseThrow(NotFoundException::new);

        apuracaoRepository.delete(apuracao);

        relatorioService.processaPorDataUF(apuracao.getData(), apuracao.getUf());
    }

    protected Apuracao quebrar(String linha){

        System.out.println(linha);

        String[] split = linha.split("\t");

        return Apuracao.builder()
                .data(DateUtils.toLocalDate(split[1]))
                .empresa(EmpresaEnum.valueOf(split[0]))
                .uf(split[2])
                .saldoCreditAnterior(CurrencyUtils.toDouble(split[3]))
                .saida(CurrencyUtils.toDouble(split[4]))
                .outroDebito(CurrencyUtils.toDouble(split[5]))
                .tDebito(CurrencyUtils.toDouble(split[6]))
                .entrada(CurrencyUtils.toDouble(split[7]))
                .outroCredito(CurrencyUtils.toDouble(split[8]))
                .tCredito(CurrencyUtils.toDouble(split[9]))
                .deducaoRedutora(CurrencyUtils.toDouble(split[10]))
                .deducaoCredora(CurrencyUtils.toDouble(split[11]))
                .saldoCredor(CurrencyUtils.toDouble(split[12]))
                .saldoDevedor(CurrencyUtils.toDouble(split[13]))
                .ressarcimentoProprio(CurrencyUtils.toDouble(split[14]))
                .ressarcimentoST(CurrencyUtils.toDouble(split[15]))
                .centralizacaoDebito(CurrencyUtils.toDouble(split[16]))
                .centralizacaoCredito(CurrencyUtils.toDouble(split[17]))
                .concentracaoDebito(CurrencyUtils.toDouble(split[18]))
                .concentracaoCredito(CurrencyUtils.toDouble(split[19]))
                .totalPagtoRealizadoFechado(CurrencyUtils.toDouble(split[20]))
                .build();
    }

    protected void salvar(Apuracao apuracao){
        apuracaoRepository.save(apuracao);
    }

    private ApuracaoResponse getResponse(Apuracao apuracao){
        ApuracaoResponse response = new ApuracaoResponse();
        BeanUtils.copyProperties(apuracao, response);

        return response;
    }

}
