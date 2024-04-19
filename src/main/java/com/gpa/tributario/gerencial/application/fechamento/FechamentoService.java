package com.gpa.tributario.gerencial.application.fechamento;

import com.gpa.tributario.gerencial.application.ArquivoBaseService;
import com.gpa.tributario.gerencial.application.fechamento.request.FechamentoRequest;
import com.gpa.tributario.gerencial.application.fechamento.response.FechamentoResponse;
import com.gpa.tributario.gerencial.application.relatorio.RelatorioService;
import com.gpa.tributario.gerencial.entity.Fechamento;
import com.gpa.tributario.gerencial.enuns.EmpresaEnum;
import com.gpa.tributario.gerencial.infrastructure.exception.NotFoundException;
import com.gpa.tributario.gerencial.repository.FechamentoRepository;
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
public class FechamentoService extends ArquivoBaseService<Fechamento> {

    @Value("${arquivo.fechamento.path}")
    private String arquivo;

    @Autowired
    private RelatorioService relatorioService;

    @Autowired
    private FechamentoRepository fechamentoRepository;

    public FechamentoResponse buscarPorID(String id)  {

        Fechamento fechamento = fechamentoRepository.findById(id).orElseThrow(NotFoundException::new);

        return getResponse(fechamento);
    }

    public List<FechamentoResponse> buscarPorDataIdLancamentoUf(LocalDate data, Integer idLancamento, String uf){

        List<Fechamento> lst = fechamentoRepository.findByDataFechamentoAndIdLancamentoAndUfOperacao(data, idLancamento, uf.toUpperCase());

        return lst.stream().map(this::getResponse).toList();
    }

    public List<FechamentoResponse> buscarPorDataUf(LocalDate data, String uf){

        List<Fechamento> lst = fechamentoRepository.findByDataFechamentoAndUfOperacaoOrderByTipo(data, uf.toUpperCase());

        return lst.stream().map(this::getResponse).toList();
    }

    public List<FechamentoResponse> buscarPorDataUfTipo(LocalDate data, String uf, String tipo){

        List<Fechamento> lst = fechamentoRepository.findByDataFechamentoAndUfOperacaoAndTipoOrderByTipo(data, uf.toUpperCase(), tipo);

        return lst.stream().map(this::getResponse).toList();
    }

    public List<FechamentoResponse> buscarPorDataUfLancamento(LocalDate data, String uf, String lancamento){

        List<Fechamento> lst = fechamentoRepository.findByDataFechamentoAndUfOperacaoAndLancamento(data, uf.toUpperCase(), lancamento);

        return lst.stream().map(this::getResponse).toList();
    }

    public List<FechamentoResponse> buscarPorDataUfLancamentoTipo(LocalDate data, String uf, String lancamento, String tipo){

        List<Fechamento> lst = fechamentoRepository.findByDataFechamentoAndUfOperacaoAndLancamentoAndTipo(data, uf.toUpperCase(), lancamento, tipo);

        return lst.stream().map(this::getResponse).toList();
    }

    public List<FechamentoResponse> buscarPorData(LocalDate data){

        List<Fechamento> lst = fechamentoRepository.findByDataFechamento(data);

        return lst.stream().map(this::getResponse).toList();
    }

    public List<FechamentoResponse> buscarPorIdLancamento(Integer idLancamento){

        List<Fechamento> lst = fechamentoRepository.findByIdLancamento(idLancamento);

        return lst.stream().map(this::getResponse).toList();
    }

    public List<FechamentoResponse> buscaGraficoComparacao(String id){

        Fechamento fechamento = fechamentoRepository.findById(id).orElseThrow(NotFoundException::new);
        List<Fechamento> lst = fechamentoRepository.findByCompare(fechamento.getDataFechamento().minusYears(1),
                                                                fechamento.getDataFechamento(),
                                                                fechamento.getUfOperacao(),
                                                                fechamento.getEmpresa(),
                                                                fechamento.getTipo(),
                                                                fechamento.getLancamento());
        return lst.stream().map(this::getResponse).toList();
    }

    public List<FechamentoResponse> buscaTodosEventosPorLancamento(LocalDate data, String uf, String lancamento){
        List<Fechamento> lst = fechamentoRepository.findTipoLancamentoGroupBy(data, uf.toUpperCase(), lancamento);
        return lst.stream().map(this::getResponse).toList();
    }

    public List<FechamentoResponse> buscaTodosEventosPorUf(LocalDate data, String uf){
        List<Fechamento> lst = fechamentoRepository.findTipoUfGroupBy(data, uf.toUpperCase());
        return lst.stream().map(this::getResponse).toList();
    }

    public List<FechamentoResponse> buscaTodosLancamentos(LocalDate data, String uf){

        List<Fechamento> lst = fechamentoRepository.findLancamentoGroupBy(data, uf.toUpperCase());
        return lst.stream().map(this::getResponse).toList();

    }

    public FechamentoResponse inserir(FechamentoRequest request){

        Fechamento fechamento = new Fechamento();
        BeanUtils.copyProperties(request, fechamento);

        salvar(fechamento);

        relatorioService.processaPorDataUF(fechamento.getDataFechamento(), fechamento.getUfOperacao());

        return getResponse(fechamento);
    }

    public FechamentoResponse alterar(String id, FechamentoRequest request){

        Fechamento fechamento = fechamentoRepository.findById(id).orElseThrow(NotFoundException::new);
        BeanUtils.copyProperties(request, fechamento);

        salvar(fechamento);

        relatorioService.processaPorDataUF(fechamento.getDataFechamento(), fechamento.getUfOperacao());

        return getResponse(fechamento);
    }

    public void remover(String id){

        Fechamento fechamento = fechamentoRepository.findById(id).orElseThrow(NotFoundException::new);

        fechamentoRepository.delete(fechamento);

        relatorioService.processaPorDataUF(fechamento.getDataFechamento(), fechamento.getUfOperacao());
    }

    public void importAll(){
        fechamentoRepository.deleteAll();
        try {
            lerArquivo(new InputStreamReader(new FileInputStream(arquivo), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected Fechamento quebrar(String linha){

        System.out.println(linha);

        String[] split = linha.split("\t");

        Fechamento fechamento = new Fechamento();
        fechamento.setDataFechamento(DateUtils.toLocalDate(split[14]));
        fechamento.setIdLancamento(Integer.parseInt(split[0]));
        fechamento.setLog(split[1]);
        fechamento.setUfOperacao(split[2]);
        fechamento.setUfResponsavelPgto(split[3]);
        fechamento.setLancamentoConferido(!"Não".equals(split[4]));
        fechamento.setAnalistaExecutor(split[5]);
        fechamento.setAnalistaValidador(split[6]);
        fechamento.setResponsavelInput(split[7]);
        fechamento.setEmpresa(EmpresaEnum.valueOf(split[8]));
        fechamento.setLancamento(split[9]);
        fechamento.setTipo(split[10]);
        fechamento.setTributacaoRelativa(split[11]);
        fechamento.setMonetizacao(!"Não".equals(split[12]));
        fechamento.setImpactaFechamento(!"Não".equals(split[13]));
        fechamento.setCompetencia(DateUtils.toLocalDate(split[15]));
        fechamento.setCaixa(DateUtils.toLocalDate(split[16]));
        fechamento.setValorTributarioApuracao(CurrencyUtils.toDouble(split[17]));
        fechamento.setAjustePositivo(CurrencyUtils.toDouble(split[18]));
        fechamento.setAjusteNegativo(CurrencyUtils.toDouble(split[19]));
        fechamento.setValorTributarioCompliance(CurrencyUtils.toDouble(split[20]));
        fechamento.setConferenciaValores(split[21]);
        fechamento.setAjusteValorProcessadoUtilizado(CurrencyUtils.toDouble(split[22]));
        fechamento.setObservacoesGerais(split.length > 23 ? split[23] : "");
        fechamento.setQualAcaoRealizar(split.length > 24 ? split[24] : "");

        return fechamento;
    }

    protected void salvar(Fechamento fechamento){

        fechamentoRepository.save(fechamento);
    }

    private FechamentoResponse getResponse(Fechamento fechamento){

        FechamentoResponse response = new FechamentoResponse();
        BeanUtils.copyProperties(fechamento, response);

        return response;
    }

}
