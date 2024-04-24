package com.gpa.tributario.gerencial.application.monetizacao;

import com.gpa.tributario.gerencial.application.ArquivoBaseService;
import com.gpa.tributario.gerencial.application.monetizacao.request.MonetizacaoRequest;
import com.gpa.tributario.gerencial.application.monetizacao.response.MonetizacaoResponse;
import com.gpa.tributario.gerencial.application.relatorio.RelatorioService;
import com.gpa.tributario.gerencial.entity.Monetizacao;
import com.gpa.tributario.gerencial.infrastructure.exception.NotFoundException;
import com.gpa.tributario.gerencial.repository.MonetizacaoRepository;
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

@Service
public class MonetizacaoService extends ArquivoBaseService<Monetizacao> {

    @Value("${arquivo.monetizacao.path}")
    private String arquivo;

    @Autowired
    private RelatorioService relatorioService;

    @Autowired
    private MonetizacaoRepository monetizacaoRepository;

    public MonetizacaoResponse buscaPorID(String id){

        Monetizacao monetizacao = monetizacaoRepository.findById(id).orElseThrow(NotFoundException::new);
        return getResponse(monetizacao);
    }

    public List<MonetizacaoResponse> buscaPorData(LocalDate data){

        List<Monetizacao> lst = monetizacaoRepository.findByData(data);

        return lst.stream().map(this::getResponse).toList();
    }

    public List<MonetizacaoResponse> buscaPorDataUf(LocalDate data, String uf){

        List<Monetizacao> lst = monetizacaoRepository.findByDataAndUf(data, uf.toUpperCase());

        return lst.stream().map(this::getResponse).toList();
    }

    public MonetizacaoResponse inserir(MonetizacaoRequest request){

        Monetizacao monetizacao = new Monetizacao();
        BeanUtils.copyProperties(request, monetizacao);

        salvar(monetizacao);

        relatorioService.processaPorDataUF(monetizacao.getData(), monetizacao.getUf());
        return getResponse(monetizacao);
    }

    public MonetizacaoResponse alterar(String id, MonetizacaoRequest request){

        Monetizacao monetizacao = monetizacaoRepository.findById(id).orElseThrow(NotFoundException::new);
        BeanUtils.copyProperties(request, monetizacao);

        salvar(monetizacao);

        relatorioService.processaPorDataUF(monetizacao.getData(), monetizacao.getUf());
        return getResponse(monetizacao);
    }

    public void remover(String id){

        Monetizacao monetizacao = monetizacaoRepository.findById(id).orElseThrow(NotFoundException::new);
        monetizacaoRepository.delete(monetizacao);
        relatorioService.processaPorDataUF(monetizacao.getData(), monetizacao.getUf());
    }

    public void importAll(){
        monetizacaoRepository.deleteAll();
        try {
            lerArquivo(new InputStreamReader(new FileInputStream(arquivo), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected Monetizacao quebrar(String linha){

        System.out.println(linha);

        String[] split = linha.split("\t");

        return Monetizacao.builder()
                .data(DateUtils.toLocalDate(split[0]))
                .uf(split[1])
                .saldoAnterior(CurrencyUtils.toDouble(split[2]))
                .ajuste(CurrencyUtils.toDouble(split[3]))
                .movimentacao(CurrencyUtils.toDouble(split[4]))
                .monetizacao(CurrencyUtils.toDouble(split[5]))
                .saldoFinal(CurrencyUtils.toDouble(split[6]))
                .estimado(CurrencyUtils.toDouble(split[8]))
                .realizado(CurrencyUtils.toDouble(split[9]))
                .build();

    }

    protected void salvar(Monetizacao monetizacao){
        monetizacaoRepository.save(monetizacao);
    }

    public MonetizacaoResponse getResponse(Monetizacao monetizacao){
        MonetizacaoResponse response = new MonetizacaoResponse();
        BeanUtils.copyProperties(monetizacao, response);
        return response;
    }
}
