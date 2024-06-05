package com.gpa.tributario.gerencial.application.eventoFechamento;

import com.gpa.tributario.gerencial.application.eventoFechamento.request.EventoFechamentoRequest;
import com.gpa.tributario.gerencial.application.eventoFechamento.response.EventoFechamentoResponse;
import com.gpa.tributario.gerencial.application.usuario.UsuarioService;
import com.gpa.tributario.gerencial.application.usuario.response.UsuarioResponse;
import com.gpa.tributario.gerencial.entity.EventoFechamento;
import com.gpa.tributario.gerencial.infrastructure.exception.NotFoundException;
import com.gpa.tributario.gerencial.repository.EventoFechamentoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventoFechamentoService {

    @Autowired
    private EventoFechamentoRepository repository;

    @Autowired
    private UsuarioService usuarioService;

    public void insert(EventoFechamentoRequest request){

        EventoFechamento eventoFechamento = new EventoFechamento();
        BeanUtils.copyProperties(request, eventoFechamento);

        repository.save(eventoFechamento);
    }

    public List<EventoFechamentoResponse> buscaTodos(){
        return repository.findAll().stream().map(this::getResponse).toList();
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
        repository.save(eventoFechamento);
        return getResponse(eventoFechamento);
    }

    public void zerar(){
        List<EventoFechamento> lst = repository.findAll();
        lst.forEach(e -> {
            e.setNome(null);
            e.setUserName(null);
            e.setConcluido(false);

            repository.save(e);
        });
    }

    public void delete(String id){
        EventoFechamento eventoFechamento = repository.findById(id).orElseThrow(NotFoundException::new);
        repository.delete(eventoFechamento);
    }

    public void alteraStatus(String userName, String id, boolean concluido){
        UsuarioResponse usuarioResponse = usuarioService.buscaPorId(userName.toUpperCase());
        EventoFechamento eventoFechamento = repository.findById(id).orElseThrow(NotFoundException::new);

        eventoFechamento.setUserName(usuarioResponse.getUserName());
        eventoFechamento.setNome(usuarioResponse.getNome());
        eventoFechamento.setConcluido(concluido);

        repository.save(eventoFechamento);

    }

    private EventoFechamentoResponse getResponse(EventoFechamento eventoFechamento){
        EventoFechamentoResponse response = new EventoFechamentoResponse();
        BeanUtils.copyProperties(eventoFechamento, response);
        return response;
    }
}
