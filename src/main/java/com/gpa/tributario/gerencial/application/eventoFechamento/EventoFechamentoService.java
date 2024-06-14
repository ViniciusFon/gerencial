package com.gpa.tributario.gerencial.application.eventoFechamento;

import com.gpa.tributario.gerencial.application.eventoFechamento.request.EventoFechamentoRequest;
import com.gpa.tributario.gerencial.application.eventoFechamento.response.EventoFechamentoResponse;
import com.gpa.tributario.gerencial.application.eventoFechamento.response.EventoFechamentoTotaisResponse;
import com.gpa.tributario.gerencial.dto.CountDto;
import com.gpa.tributario.gerencial.entity.EventoFechamento;
import com.gpa.tributario.gerencial.entity.HistoricoEventoFechamento;
import com.gpa.tributario.gerencial.infrastructure.exception.NotFoundException;
import com.gpa.tributario.gerencial.infrastructure.security.SecurityUtil;
import com.gpa.tributario.gerencial.infrastructure.security.UserDetailsImpl;
import com.gpa.tributario.gerencial.repository.EventoFechamentoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EventoFechamentoService {

    @Autowired
    private EventoFechamentoRepository repository;

    @Autowired
    private SimpMessagingTemplate template;

    public void insert(EventoFechamentoRequest request){

        EventoFechamento eventoFechamento = new EventoFechamento();
        BeanUtils.copyProperties(request, eventoFechamento);
        eventoFechamento.setUF(request.getEstado());

        repository.save(eventoFechamento);
    }

    public List<EventoFechamentoResponse> buscaTodos(){
        return repository.findAllByOrderByUF().stream().map(this::getResponse).toList();
    }

    public List<EventoFechamentoResponse> buscaPorUF(String uf){
        return repository.findByUF(uf.toUpperCase()).stream().map(this::getResponse).toList();
    }

    public EventoFechamentoResponse buscaPorID(String id){
        EventoFechamento eventoFechamento = repository.findById(id).orElseThrow(NotFoundException::new);
        return getResponse(eventoFechamento);
    }

    public EventoFechamentoResponse alterar(String id, EventoFechamentoRequest request){
        EventoFechamento eventoFechamento = repository.findById(id).orElseThrow(NotFoundException::new);
        BeanUtils.copyProperties(request, eventoFechamento);
        eventoFechamento.setUF(request.getEstado());
        repository.save(eventoFechamento);
        return getResponse(eventoFechamento);
    }

    public void zerar(){
        List<EventoFechamento> lst = repository.findAll();
        lst.forEach(e -> {
            e.setNome(null);
            e.setConcluido(false);
            e.setHistorico(null);

            repository.save(e);
        });

        atualizaStatusWebSocket();
    }

    public void delete(String id){
        EventoFechamento eventoFechamento = repository.findById(id).orElseThrow(NotFoundException::new);
        repository.delete(eventoFechamento);
    }

    public void alteraStatus(String id, boolean concluido){
        UserDetailsImpl userDetails = SecurityUtil.getUserData();

        EventoFechamento eventoFechamento = repository.findById(id).orElseThrow(NotFoundException::new);
        eventoFechamento.setConcluido(concluido);
        eventoFechamento.setNome(userDetails.getUser().getNome());

        HistoricoEventoFechamento historico = new HistoricoEventoFechamento();
        historico.setNome(userDetails.getUser().getNome());
        historico.setUserName(userDetails.getUser().getUserName());
        historico.setDataHora(LocalDateTime.now());
        historico.setAcao("Alter Status to: " + concluido);

        eventoFechamento.addHistorico(historico);
        repository.save(eventoFechamento);

        atualizaStatusWebSocket();
    }

    @Async
    private void atualizaStatusWebSocket(){
        template.convertAndSend("/topic/grafico", buscaTotais());
        template.convertAndSend("/topic/lista", buscaTodos());
    }

    public void alteraNome(String id, String nome){
        UserDetailsImpl userDetails = SecurityUtil.getUserData();

        EventoFechamento eventoFechamento = repository.findById(id).orElseThrow(NotFoundException::new);
        eventoFechamento.setNome(nome);

        HistoricoEventoFechamento historico = new HistoricoEventoFechamento();
        historico.setNome(userDetails.getUser().getNome());
        historico.setUserName(userDetails.getUser().getUserName());
        historico.setDataHora(LocalDateTime.now());
        historico.setAcao("Alter Name to: " + nome);

        eventoFechamento.addHistorico(historico);

        repository.save(eventoFechamento);

    }

    public List<EventoFechamentoTotaisResponse> buscaTotais(){
        List<CountDto> lstCount = repository.findCountGroupByUf();
        List<CountDto> lstConcluidos = repository.findCountConcluidoGroupByUf();

        return lstCount.stream().map(c -> {
            EventoFechamentoTotaisResponse evento = new EventoFechamentoTotaisResponse();
            evento.setUF(c.getChave());
            evento.setQuantidade(c.getTotal());
            evento.setConcluidos(getConcluido(lstConcluidos, c.getChave()));
            evento.setPendentes(evento.getQuantidade() - evento.getConcluidos());
            return evento;
        }).toList();
    }

    private Long getConcluido(List<CountDto> lst, String uf){
        Optional<CountDto> count = lst.stream().filter(c -> c.getChave().equals(uf)).findFirst();
        return count.map(CountDto::getTotal).orElse(0L);
    }

    private EventoFechamentoResponse getResponse(EventoFechamento eventoFechamento){
        EventoFechamentoResponse response = new EventoFechamentoResponse();
        BeanUtils.copyProperties(eventoFechamento, response);
        return response;
    }
}
