package com.gpa.tributario.gerencial.application.eventoObrigacoes;

import com.gpa.tributario.gerencial.application.ArquivoBaseService;
import com.gpa.tributario.gerencial.application.eventoObrigacoes.request.EventoObrigacoesRequest;
import com.gpa.tributario.gerencial.application.eventoObrigacoes.response.EventoObrigacoesResponse;
import com.gpa.tributario.gerencial.application.eventoObrigacoes.response.EventoObrigacoesTotaisResponse;
import com.gpa.tributario.gerencial.dto.CountDto;
import com.gpa.tributario.gerencial.entity.HistoricoEventoObrigacoes;
import com.gpa.tributario.gerencial.entity.Obrigacoes;
import com.gpa.tributario.gerencial.enuns.EmpresaEnum;
import com.gpa.tributario.gerencial.enuns.ObrigacaoEnum;
import com.gpa.tributario.gerencial.infrastructure.exception.BusinessException;
import com.gpa.tributario.gerencial.infrastructure.exception.NotFoundException;
import com.gpa.tributario.gerencial.infrastructure.security.SecurityUtil;
import com.gpa.tributario.gerencial.infrastructure.security.UserDetailsImpl;
import com.gpa.tributario.gerencial.repository.ObrigacoesRepository;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
public class EventoObrigacoesService extends ArquivoBaseService<Obrigacoes> {

    @Autowired
    private ObrigacoesRepository repository;

    @Autowired
    private SimpMessagingTemplate template;

    private String arquivo = "C:\\Users\\tc024145\\Documents\\Arquivos\\Controle Obrigações_08.20242.txt";

    public void insert(EventoObrigacoesRequest request){

        Obrigacoes eventoObrigacoes = new Obrigacoes();
        BeanUtils.copyProperties(request, eventoObrigacoes);
        eventoObrigacoes.setUF(request.getEstado());
        if(StringUtils.isBlank(request.getUfDestino())){
            eventoObrigacoes.setUfDestino(request.getEstado());
        }

        repository.save(eventoObrigacoes);
    }

    public List<EventoObrigacoesResponse> buscaTodos(){
        return repository.findAllByOrderByUF().stream().map(this::getResponse).toList();
    }

    public List<EventoObrigacoesResponse> buscaPorUF(String uf){
        return repository.findByUF(uf.toUpperCase()).stream().map(this::getResponse).toList();
    }

    public EventoObrigacoesResponse buscaPorID(String id){
        Obrigacoes eventoObrigacoes = repository.findById(id).orElseThrow(NotFoundException::new);
        return getResponse(eventoObrigacoes);
    }

    public EventoObrigacoesResponse alterar(String id, EventoObrigacoesRequest request){
        Obrigacoes eventoObrigacoes = repository.findById(id).orElseThrow(NotFoundException::new);
        BeanUtils.copyProperties(request, eventoObrigacoes);
        eventoObrigacoes.setUF(request.getEstado());
        if(StringUtils.isBlank(request.getUfDestino())){
            eventoObrigacoes.setUfDestino(request.getEstado());
        }
        repository.save(eventoObrigacoes);
        return getResponse(eventoObrigacoes);
    }

    public void recebeGia(MultipartFile file){
        if(file.isEmpty()) throw new BusinessException("O Arquivo está vazio");

        UserDetailsImpl userDetails = SecurityUtil.getUserData();
        String nome = userDetails.getUser().getNome();
        String userName = userDetails.getUsername();

        try {
            PdfReader reader = new PdfReader(file.getInputStream());
            int pages = reader.getNumberOfPages();
            for (int i = 1; i <= pages; i++) {
                buscaValor(PdfTextExtractor.getTextFromPage(reader, i), nome, userName);
            }
            reader.close();
            atualizaStatusWebSocket();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private  void buscaValor(String texto, String nome, String userName){
        String[] linhas = texto.split("\n");
        for(String l : linhas){
            if(l.startsWith("IE:")){
                String[] valores = l.split(" ");
                if("SIM".equals(valores[3])){
                    preencheGia(valores[1], nome, userName);
                }
            }
        }
    }

    private void preencheGia(String ie, String nome, String userName){
        Obrigacoes eventoObrigacoes = repository.findByIeAndObrigacao(ie.replaceAll("\\D",""), ObrigacaoEnum.GIA_SP).orElse(null);
        if(isNull(eventoObrigacoes)) return;

        eventoObrigacoes.setConcluido(true);
        eventoObrigacoes.setNome(nome);

        HistoricoEventoObrigacoes historico = new HistoricoEventoObrigacoes();
        historico.setNome(nome);
        historico.setUserName(userName);
        historico.setDataHora(LocalDateTime.now());
        historico.setAcao("Status changed to: " + true);

        eventoObrigacoes.addHistorico(historico);
        repository.save(eventoObrigacoes);
    }
    public void recebeSped(List<String> arquivos){
        UserDetailsImpl userDetails = SecurityUtil.getUserData();
        String nome = userDetails.getUser().getNome();
        String userName = userDetails.getUsername();
        for(String nomeArq: arquivos){
            if(!nomeArq.toLowerCase().endsWith(".rec")) continue;

            String loja = getLoja(nomeArq);
            if(!isNull(loja)) {
                Obrigacoes eventoObrigacoes = repository.findByLojaAndObrigacao(loja, ObrigacaoEnum.SPED).orElse(null);
                if(isNull(eventoObrigacoes)) continue;

                eventoObrigacoes.setConcluido(true);
                eventoObrigacoes.setNome(nome);

                HistoricoEventoObrigacoes historico = new HistoricoEventoObrigacoes();
                historico.setNome(nome);
                historico.setUserName(userName);
                historico.setDataHora(LocalDateTime.now());
                historico.setAcao("Status changed to: " + true);

                eventoObrigacoes.addHistorico(historico);
                repository.save(eventoObrigacoes);
            }
        }

        atualizaStatusWebSocket();
    }

    private String getLoja(String nome){
        String[] split = nome.split("_");

        if(!split[2].startsWith("lj")) return null;

        return split[2].replace("lj","");
    }

    public void zerar(){
        List<Obrigacoes> lst = repository.findAll();
        lst.forEach(e -> {
            e.setNome(null);
            e.setConcluido(false);

            repository.save(e);
        });

        atualizaStatusWebSocket();
    }

    public void zerarTudo(){
        List<Obrigacoes> lst = repository.findAll();
        lst.forEach(e -> {
            e.setNome(null);
            e.setConcluido(false);
            e.setHistorico(null);

            repository.save(e);
        });

        atualizaStatusWebSocket();
    }

    public void delete(String id){
        Obrigacoes eventoObrigacoes = repository.findById(id).orElseThrow(NotFoundException::new);
        repository.delete(eventoObrigacoes);
    }

    public void alteraStatus(String id, boolean concluido){
        UserDetailsImpl userDetails = SecurityUtil.getUserData();

        Obrigacoes eventoObrigacoes = repository.findById(id).orElseThrow(NotFoundException::new);
        eventoObrigacoes.setConcluido(concluido);
        eventoObrigacoes.setNome(userDetails.getUser().getNome());

        HistoricoEventoObrigacoes historico = new HistoricoEventoObrigacoes();
        historico.setNome(userDetails.getUser().getNome());
        historico.setUserName(userDetails.getUser().getUserName());
        historico.setDataHora(LocalDateTime.now());
        historico.setAcao("Status changed to: " + concluido);

        eventoObrigacoes.addHistorico(historico);
        repository.save(eventoObrigacoes);

        atualizaStatusWebSocket();
    }

    public void alteraNome(String id, String nome){
        UserDetailsImpl userDetails = SecurityUtil.getUserData();

        Obrigacoes eventoObrigacoes = repository.findById(id).orElseThrow(NotFoundException::new);
        eventoObrigacoes.setNome(nome);

        HistoricoEventoObrigacoes historico = new HistoricoEventoObrigacoes();
        historico.setNome(userDetails.getUser().getNome());
        historico.setUserName(userDetails.getUser().getUserName());
        historico.setDataHora(LocalDateTime.now());
        historico.setAcao("Alter Name to: " + nome);

        eventoObrigacoes.addHistorico(historico);

        repository.save(eventoObrigacoes);

    }

    public List<EventoObrigacoesTotaisResponse> buscaTotais(){
        List<CountDto> lstCount = repository.findCountGroupByUf();
        List<CountDto> lstConcluidos = repository.findCountConcluidoGroupByUf();

        return lstCount.stream().map(c -> {
            EventoObrigacoesTotaisResponse evento = new EventoObrigacoesTotaisResponse();
            evento.setUF(c.getChave());
            evento.setQuantidade(c.getTotal());
            evento.setConcluidos(getConcluido(lstConcluidos, c.getChave()));
            evento.setPendentes(evento.getQuantidade() - evento.getConcluidos());
            return evento;
        }).toList();
    }

    @Async
    private void atualizaStatusWebSocket(){
        template.convertAndSend("/topic/graficoObrigacoes", buscaTotais());
        template.convertAndSend("/topic/listaObrigacoes", buscaTodos());
    }

    private Long getConcluido(List<CountDto> lst, String uf){
        Optional<CountDto> count = lst.stream().filter(c -> c.getChave().equals(uf)).findFirst();
        return count.map(CountDto::getTotal).orElse(0L);
    }

    private EventoObrigacoesResponse getResponse(Obrigacoes eventoObrigacoes){
        EventoObrigacoesResponse response = new EventoObrigacoesResponse();
        BeanUtils.copyProperties(eventoObrigacoes, response);
        return response;
    }

    public void importAll(){
        repository.deleteAll();
        try {
            lerArquivo(new InputStreamReader(new FileInputStream(arquivo), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Obrigacoes quebrar(String linha) {

        String[] split = linha.split("\t");

        return Obrigacoes.builder()
                .empresa(EmpresaEnum.valueOf(split[1]))
                .UF(split[2].trim())
                .ufDestino(split[3].trim())
                .loja(lPad(split[4]))
                .cnpj(split[5])
                .ie(split[6])
                .obrigacao(ObrigacaoEnum.valueOf(getObr(split[10])))
                .build();
    }

    private String lPad(String value){
        if (value.length() < 4) {
            return StringUtils.leftPad(value, 4, "0");
        }

        return value;
    }

    @Override
    protected void salvar(Obrigacoes entity) {
        repository.save(entity);
    }

    private String getObr(String o){
        return o.trim().replace(" ", "_");
    }
}
