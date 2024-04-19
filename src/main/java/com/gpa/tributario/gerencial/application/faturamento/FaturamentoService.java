package com.gpa.tributario.gerencial.application.faturamento;

import com.gpa.tributario.gerencial.application.ArquivoBaseService;
import com.gpa.tributario.gerencial.application.faturamento.request.FaturamentoRequest;
import com.gpa.tributario.gerencial.application.faturamento.response.FaturamentoResponse;
import com.gpa.tributario.gerencial.application.relatorio.RelatorioService;
import com.gpa.tributario.gerencial.entity.Faturamento;
import com.gpa.tributario.gerencial.entity.Icms;
import com.gpa.tributario.gerencial.infrastructure.exception.NotFoundException;
import com.gpa.tributario.gerencial.repository.FaturamentoRepository;
import com.gpa.tributario.gerencial.repository.IcmsRepository;
import com.gpa.tributario.gerencial.utils.CurrencyUtils;
import com.gpa.tributario.gerencial.utils.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class FaturamentoService extends ArquivoBaseService<Faturamento> {

    @Value("${arquivo.faturamento.path}")
    private String arquivo;

    @Autowired
    private IcmsRepository icmsRepository;

    @Autowired
    private RelatorioService relatorioService;

    @Autowired
    private FaturamentoRepository faturamentoRepository;

    public FaturamentoResponse buscaPorID(String id){

        Faturamento faturamento = faturamentoRepository.findById(id).orElseThrow(NotFoundException::new);
        return getResponse(faturamento);
    }

    public List<FaturamentoResponse> buscaPorDataUf(LocalDate data, String uf){

        List<Faturamento> lst = faturamentoRepository.findByDataAndUf(data, uf.toUpperCase());

        return lst.stream().map(this::getResponse).toList();
    }

    public List<FaturamentoResponse> buscaPorData(LocalDate data){

        List<Faturamento> lst = faturamentoRepository.findByData(data);

        return lst.stream().map(this::getResponse).toList();
    }

    public List<FaturamentoResponse> buscaPorUf(String uf){

        List<Faturamento> lst = faturamentoRepository.findByUf(uf.toUpperCase());

        return lst.stream().map(this::getResponse).toList();
    }

    public FaturamentoResponse inserir(FaturamentoRequest request){

        Faturamento faturamento = new Faturamento();
        BeanUtils.copyProperties(request, faturamento);
        faturamento.setIcmsVendas(buscaSomaICMS(request.getData(), request.getUf()));

        salvar(faturamento);

        relatorioService.processaPorDataUF(faturamento.getData(), faturamento.getUf());

        return getResponse(faturamento);
    }

    public FaturamentoResponse alterar(String id, FaturamentoRequest request){
        Faturamento faturamento = faturamentoRepository.findById(id).orElseThrow(NotFoundException::new);
        BeanUtils.copyProperties(request, faturamento);
        faturamento.setIcmsVendas(buscaSomaICMS(request.getData(), request.getUf()));

        salvar(faturamento);

        relatorioService.processaPorDataUF(faturamento.getData(), faturamento.getUf());

        return getResponse(faturamento);
    }

    public void remover(String id){
        Faturamento faturamento = faturamentoRepository.findById(id).orElseThrow(NotFoundException::new);
        faturamentoRepository.delete(faturamento);
    }

    public void importAll(){
        faturamentoRepository.deleteAll();
        try {
            lerArquivo(new InputStreamReader(new FileInputStream(arquivo), "UTF-8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private FaturamentoResponse getResponse(Faturamento faturamento){
        FaturamentoResponse response = new FaturamentoResponse();
        BeanUtils.copyProperties(faturamento, response);
        return response;
    }

    private double buscaSomaICMS(LocalDate data, String codEstado){

        List<Icms> lstIcms = icmsRepository.findByDataIcmsAndCodEstadoAndDescricao(data, codEstado.toUpperCase(), "VENDA");

        return lstIcms.stream()
                .mapToDouble(Icms::getValorIcms) // Obtém o valorIcms de cada objeto
                .sum();

    }

    @Override
    protected Faturamento quebrar(String linha) {
        System.out.println(linha);

        String[] split = linha.split("\t");

        return Faturamento.builder()
                .data(DateUtils.toLocalDate(split[0]))
                .uf(split[1])
                .faturamento(CurrencyUtils.toDouble(split[2]))
                .icmsVendas(buscaSomaICMS(DateUtils.toLocalDate(split[0]), split[1]))
                .build();
    }

    @Override
    protected void salvar(Faturamento faturamento){
        faturamentoRepository.save(faturamento);
    }

}
