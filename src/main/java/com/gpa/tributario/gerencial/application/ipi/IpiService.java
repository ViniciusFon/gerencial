package com.gpa.tributario.gerencial.application.ipi;

import com.gpa.tributario.gerencial.application.ArquivoBaseService;
import com.gpa.tributario.gerencial.application.ipi.request.IpiRequest;
import com.gpa.tributario.gerencial.application.ipi.response.IpiResponse;
import com.gpa.tributario.gerencial.application.relatorio.RelatorioService;
import com.gpa.tributario.gerencial.entity.Ipi;
import com.gpa.tributario.gerencial.infrastructure.exception.NotFoundException;
import com.gpa.tributario.gerencial.repository.IpiRepository;
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
public class IpiService extends ArquivoBaseService<Ipi> {

    @Value("${arquivo.ipi.path}")
    private String arquivo;

    @Autowired
    private RelatorioService relatorioService;

    @Autowired
    private IpiRepository ipiRepository;

    public IpiResponse buscaPorID(String id){

        Ipi ipi = ipiRepository.findById(id).orElseThrow(NotFoundException::new);

        return getResponse(ipi);
    }

    public List<IpiResponse> buscaPorData(LocalDate data){

        List<Ipi> lst = ipiRepository.findByData(data);

        return lst.stream().map(this::getResponse).toList();
    }

    public List<IpiResponse> buscaPorDataUfLoja(LocalDate data, String uf, Integer loja){

        List<Ipi> lst = ipiRepository.findByDataAndUfAndLoja(data, uf.toUpperCase(), loja);

        return lst.stream().map(this::getResponse).toList();
    }

    public List<IpiResponse> buscaPorDataLoja(LocalDate data, Integer loja){

        List<Ipi> lst = ipiRepository.findByDataAndLoja(data, loja);

        return lst.stream().map(this::getResponse).toList();
    }

    public List<IpiResponse> buscaPoUfLoja(String uf, Integer loja){

        List<Ipi> lst = ipiRepository.findByUfAndLoja(uf.toUpperCase(), loja);

        return lst.stream().map(this::getResponse).toList();
    }

    public List<IpiResponse> buscaPorDataUf(LocalDate data, String uf){

        List<Ipi> lst = ipiRepository.findByDataAndUf(data, uf.toUpperCase());

        return lst.stream().map(this::getResponse).toList();
    }

    public IpiResponse inserir(IpiRequest request){

        Ipi ipi = new Ipi();
        BeanUtils.copyProperties(request, ipi);
        salvar(ipi);

        relatorioService.processaPorDataUF(ipi.getData(), ipi.getUf());

        return getResponse(ipi);
    }

    public IpiResponse alterar(String id, IpiRequest request){

        Ipi ipi = ipiRepository.findById(id).orElseThrow(NotFoundException::new);
        BeanUtils.copyProperties(request, ipi);
        salvar(ipi);

        relatorioService.processaPorDataUF(ipi.getData(), ipi.getUf());

        return getResponse(ipi);
    }

    public void remover(String id){

        Ipi ipi = ipiRepository.findById(id).orElseThrow(NotFoundException::new);
        ipiRepository.delete(ipi);
        relatorioService.processaPorDataUF(ipi.getData(), ipi.getUf());
    }

    public void importAll(){
        ipiRepository.deleteAll();
        try {
            lerArquivo(new InputStreamReader(new FileInputStream(arquivo), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected Ipi quebrar(String linha){

        System.out.println(linha);

        String[] split = linha.split("\t");

        return Ipi.builder()
                .data(DateUtils.toLocalDate(split[0]))
                .uf(split[1])
                .loja(Integer.parseInt(split[2]))
                .saldoCreditAnterior(CurrencyUtils.toDouble(split[3]))
                .debitoSaida(CurrencyUtils.toDouble(split[4]))
                .debitoOutro(CurrencyUtils.toDouble(split[5]))
                .debitoTdebito(CurrencyUtils.toDouble(split[6]))
                .creditoEntrada(CurrencyUtils.toDouble(split[7]))
                .creditoOutro(CurrencyUtils.toDouble(split[8]))
                .creditoTcredito(CurrencyUtils.toDouble(split[9]))
                .deducaoRedutora(CurrencyUtils.toDouble(split[10]))
                .deducaoCredora(CurrencyUtils.toDouble(split[11]))
                .saldoCredor(CurrencyUtils.toDouble(split[12]))
                .saldoDevedor(CurrencyUtils.toDouble(split[13]))
                .build();

    }

    protected void salvar(Ipi ipi){
        ipiRepository.save(ipi);
    }

    private IpiResponse getResponse(Ipi ipi){
        IpiResponse response = new IpiResponse();
        BeanUtils.copyProperties(ipi, response);
        return response;
    }
}
