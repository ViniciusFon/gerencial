package com.gpa.tributario.gerencial.application.relatorio;

import com.gpa.tributario.gerencial.application.relatorio.request.RelatorioRequest;
import com.gpa.tributario.gerencial.application.relatorio.response.RelatorioResponse;
import com.gpa.tributario.gerencial.entity.*;
import com.gpa.tributario.gerencial.infrastructure.exception.NotFoundException;
import com.gpa.tributario.gerencial.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RelatorioService {

    private final List<String> lstEstados = List.of("AC", "AL", "AM", "AP", "BA", "CE", "DF", "ES", "GO", "MA", "MG", "MS", "MT", "PA", "PB", "PE", "PI", "PR", "RJ", "RN", "RO", "RR", "RS", "SC", "SE", "SP", "TO");

    @Autowired
    private RelatorioRepository relatorioRepository;

    @Autowired
    private ObriAcessRepository obriAcessRepository;

    @Autowired
    private FaturamentoRepository faturamentoRepository;

    @Autowired
    private FechamentoRepository fechamentoRepository;

    @Autowired
    private ApuracaoRepository apuracaoRepository;

    @Autowired
    private NotaRepository notaRepository;

    @Autowired
    private MonetizacaoRepository monetizacaoRepository;

    @Autowired
    private IcmsRepository icmsRepository;

    @Autowired
    private IpiRepository ipiRepository;

    public RelatorioResponse alterar(LocalDate data, String uf, RelatorioRequest request){

        RelatorioID id = new RelatorioID(data, uf.toUpperCase());
        Relatorio relatorio = relatorioRepository.findById(id).orElseThrow(NotFoundException::new);
        relatorio.setRegimeEspecial(request.isRegimeEspecial());
        relatorio.setScoreFiscalizacao(request.getScoreFiscalizacao());

        salvar(relatorio);

        return getResponse(relatorio);
    }

    public RelatorioResponse buscarPorDataUF(LocalDate data, String uf){

        RelatorioID id = new RelatorioID(data, uf.toUpperCase());
        Relatorio relatorio = relatorioRepository.findById(id).orElseThrow(NotFoundException::new);
        return getResponse(relatorio);
    }

    public List<RelatorioResponse> buscaPorData(LocalDate data){
        List<Relatorio> lst = relatorioRepository.findByData(data);

        return lst.stream().map(this::getResponse).toList();
    }

    @Async
    public void processaPorDataUF(LocalDate data, String uf){
        RelatorioID id = new RelatorioID();
        id.setData(data);
        id.setUf(uf);

        Relatorio relatorio = relatorioRepository.findById(id).orElse(null);

        if(relatorio != null) {
            calculaValores(relatorio, data, uf);
            salvar(relatorio);

            List<Relatorio> lst = relatorioRepository.findByData(id.getData());

            processarPorcentagemEsalvar(lst);
        }
    }

    public void processaRelatorioPorData(LocalDate data){

        List<Relatorio> novaLista = new ArrayList<>();

        for(String uf : lstEstados){
            RelatorioID id = new RelatorioID();
            id.setData(data);
            id.setUf(uf);

            Relatorio relatorio = new Relatorio();
            relatorio.setId(id);

            calculaValores(relatorio, data, uf);

            novaLista.add(relatorio);
        }

        processarPorcentagemEsalvar(novaLista);

    }

    private void processarPorcentagemEsalvar(List<Relatorio> lst){

        double totalFaturamento = lst.stream().mapToDouble(Relatorio::getFaturamento).sum();
        double totalIcms =  lst.stream().mapToDouble(Relatorio::getIcmsVenda).sum();

        lst.forEach(r -> {
            r.setPorcentFatTotal((int) Math.round(r.getFaturamento()/totalFaturamento*100));
            r.setPorcentIcms(r.getIcmsVenda()/totalIcms);
            salvar(r);
        });
    }

    private Integer calculaPorcentagem(Double valor1, Double valor2){

        double result = ((valor1/valor2)-1)*100;
        return (int) Math.ceil(result);
    }

    private double arredondar(double valor){
        BigDecimal bd = new BigDecimal(valor);
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void calculaValores(Relatorio relatorio, LocalDate data, String uf){

        montaIcms(relatorio, data, uf);
        montaFaturamento(relatorio, data, uf);
        montaFechamento(relatorio, data, uf);
        montaApuracao(relatorio, data, uf);
        montaNota(relatorio, data, uf);
        montaObrigacoes(relatorio, data, uf);
        montaMonetizacao(relatorio, data, uf);
        montaIPI(relatorio, data, uf);
    }

    private void montaIcms(Relatorio relatorio, LocalDate data, String uf){

        List<Icms> lstIcms = icmsRepository.findByDataIcmsAndCodEstadoAndDescricao(data, uf.toUpperCase(), "VENDA");

        double valor = lstIcms.stream()
                .mapToDouble(Icms::getValorIcms) // Obtém o valorIcms de cada objeto
                .sum();

        relatorio.setIcmsVenda(valor);
    }
    private void montaFaturamento(Relatorio relatorio, LocalDate data, String uf) {
        List<Faturamento> lst = faturamentoRepository.findByDataAndUf(data, uf);
        double faturamento = lst.stream().mapToDouble(Faturamento::getFaturamento).sum();

        List<Faturamento> lstAnt = faturamentoRepository.findByDataAndUf(data.minusMonths(1), uf);
        double faturamentoAnt = lstAnt.stream().mapToDouble(Faturamento::getFaturamento).sum();
        relatorio.setFaturamento(faturamento);
        relatorio.setFaturamentoMesAnterior(faturamentoAnt);
        relatorio.setPorcentFatMesAnterior(calculaPorcentagem(faturamento, faturamentoAnt));
    }

    private void montaFechamento(Relatorio relatorio, LocalDate data, String uf) {
        List<Fechamento> lst = fechamentoRepository.findByDataFechamentoAndUfOperacao(data, uf.toUpperCase());
        relatorio.setQtdeArqValid(lst.size());

        double icmsProprio = lst.stream().filter(f -> "Sem efeitos".equalsIgnoreCase(f.getLancamento())).mapToDouble(Fechamento::getValorTributarioCompliance).sum();
        double icmsST = lst.stream().filter(f -> "PAGAMENTO".equalsIgnoreCase(f.getLancamento()) && "ICMS ST".equalsIgnoreCase(f.getTributacaoRelativa())).mapToDouble(Fechamento::getValorTributarioCompliance).sum()+
                        lst.stream().filter(f -> "PAGAMENTO".equalsIgnoreCase(f.getLancamento()) && "FECOP ICMS ST".equalsIgnoreCase(f.getTributacaoRelativa())).mapToDouble(Fechamento::getValorTributarioCompliance).sum();
        double vlrRecolhidoMes = lst.stream().filter(f -> "PAGAMENTO".equalsIgnoreCase(f.getLancamento())).mapToDouble(Fechamento::getValorTributarioCompliance).sum();

        double somaIcmsProprioFaturamento = 0D;
        relatorio.setIcmsProprio(icmsProprio);
        relatorio.setIcmsST(icmsST);
        relatorio.setValorRecolhidoMes(vlrRecolhidoMes);

        if(relatorio.getIcmsST() + relatorio.getIcmsProprio() != 0){
            somaIcmsProprioFaturamento = (relatorio.getIcmsST() + relatorio.getIcmsProprio() + relatorio.getIcmsVenda())/ relatorio.getFaturamento();
        }
        relatorio.setPorcentSomaIcmsPorFat(somaIcmsProprioFaturamento);
        if(relatorio.getFaturamento() == 0D)
            relatorio.setPercentFatVsRec(0D);
        else
            relatorio.setPercentFatVsRec(relatorio.getValorRecolhidoMes()/ relatorio.getFaturamento());
    }

    private void montaApuracao(Relatorio relatorio, LocalDate data, String uf) {
        List<Apuracao> lstApuracao = apuracaoRepository.findByDataAndUf(data, uf.toUpperCase());
        double somaSaldoCredor = lstApuracao.stream().mapToDouble(Apuracao::getSaldoCredor).sum();
        double somaSaldoDevedor = lstApuracao.stream().mapToDouble(Apuracao::getSaldoDevedor).sum();

        relatorio.setSaldoCredorApuracao(somaSaldoCredor);
        relatorio.setSaldoDevedorApuracao(somaSaldoDevedor);
    }

    private void montaNota(Relatorio relatorio, LocalDate data, String uf) {
        List<Nota> lstNotas = notaRepository.findByDataNotaAndUf(data, uf);
        long qtdeNotasRecebidas = lstNotas.stream().filter(n -> "Recebidas".equalsIgnoreCase(n.getEmitidarecebida())).mapToLong(Nota::getQtdeNfs).sum();
        long qtdeNotasEmitidas = lstNotas.stream().filter(n -> "Emitidas".equalsIgnoreCase(n.getEmitidarecebida())).mapToLong(Nota::getQtdeNfs).sum();
        long ctes = lstNotas.stream().mapToLong(Nota::getCtes).sum();
        relatorio.setQtdeNFRecebida(qtdeNotasRecebidas);
        relatorio.setQtdeNFEmitida(qtdeNotasEmitidas);
        relatorio.setCtes(ctes);
    }

    private void montaObrigacoes(Relatorio relatorio, LocalDate data, String uf) {
        List<ObriAcess> lstObri = obriAcessRepository.findByDataObriAndUfOrderByCodEstabelecimento(data, uf);
        long totalObrig = lstObri.stream().mapToLong(ObriAcess::getTotal).sum();
        relatorio.setObrigacoesAcessorias(totalObrig);
        relatorio.setQtdeFiliais(lstObri.size());
    }

    private void montaMonetizacao(Relatorio relatorio, LocalDate data, String uf) {
        List<Monetizacao> lstMonet = monetizacaoRepository.findByDataAndUf(data, uf);
        double saldoAnt = lstMonet.stream().mapToDouble(Monetizacao::getSaldoAnterior).sum();
        double ajustes = lstMonet.stream().mapToDouble(Monetizacao::getAjuste).sum();
        double movimentacao = lstMonet.stream().mapToDouble(Monetizacao::getMovimentacao).sum();
        double monetizacao = lstMonet.stream().mapToDouble(Monetizacao::getMonetizacao).sum();
        double saldoFinal = lstMonet.stream().mapToDouble(Monetizacao::getSaldoFinal).sum();
        double saldoEstimado = lstMonet.stream().mapToDouble(Monetizacao::getEstimado).sum();
        double saldoRealizado = lstMonet.stream().mapToDouble(Monetizacao::getRealizado).sum();
        relatorio.setSaldoAnteriorContab(saldoAnt);
        relatorio.setAjustes(ajustes);
        relatorio.setMovimentacao(movimentacao);
        relatorio.setMonetizacao(monetizacao);
        relatorio.setSaldoFinalContab(saldoFinal);
        relatorio.setSaldoEstimado(saldoEstimado);
        relatorio.setSaldoRealizado(saldoRealizado);
    }

    private void montaIPI(Relatorio relatorio, LocalDate data, String uf){
        List<Ipi> lstIPI = ipiRepository.findByDataAndUf(data, uf);
        double saldoCredor = lstIPI.stream().mapToDouble(Ipi::getSaldoCredor).sum();
        double saldoDevedor = lstIPI.stream().mapToDouble(Ipi::getSaldoDevedor).sum();
        relatorio.setSaldoCredorIPI(saldoCredor);
        relatorio.setSaldoDevedorIPI(saldoDevedor);
    }

    private void salvar(Relatorio relatorio){
        relatorioRepository.save(relatorio);
    }

    private RelatorioResponse getResponse(Relatorio relatorio){
        RelatorioResponse response = new RelatorioResponse();
        BeanUtils.copyProperties(relatorio, response);
        response.setData(relatorio.getId().getData());
        response.setUf(relatorio.getId().getUf());
        return response;
    }
}
