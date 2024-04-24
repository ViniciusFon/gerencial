package com.gpa.tributario.gerencial.application.nota;

import com.gpa.tributario.gerencial.application.ArquivoBaseService;
import com.gpa.tributario.gerencial.application.nota.request.NotaRequest;
import com.gpa.tributario.gerencial.application.nota.response.NotaResponse;
import com.gpa.tributario.gerencial.application.relatorio.RelatorioService;
import com.gpa.tributario.gerencial.entity.Nota;
import com.gpa.tributario.gerencial.enuns.EmpresaEnum;
import com.gpa.tributario.gerencial.infrastructure.exception.NotFoundException;
import com.gpa.tributario.gerencial.repository.NotaRepository;
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
public class NotaService extends ArquivoBaseService<Nota> {

    @Value("${arquivo.nota.path}")
    private String arquivo;

    @Autowired
    private RelatorioService relatorioService;

    @Autowired
    private NotaRepository notaRepository;

    public NotaResponse buscaPorID(String id){

        Nota nota = notaRepository.findById(id).orElseThrow(NotFoundException::new);
        return getResponse(nota);
    }

    public List<NotaResponse> buscaPorDataEmpresaUfCfop(LocalDate data, EmpresaEnum empresa, String uf, Integer cfop){

        List<Nota> lst = notaRepository.findByDataNotaAndEmpresaAndUfAndCfop(data, empresa, uf.toUpperCase(), cfop);
        return lst.stream().map(this::getResponse).toList();
    }

    public List<NotaResponse> buscaPorDataEmpresaUf(LocalDate data, EmpresaEnum empresa, String uf){

        List<Nota> lst = notaRepository.findByDataNotaAndEmpresaAndUf(data, empresa, uf.toUpperCase());
        return lst.stream().map(this::getResponse).toList();
    }

    public List<NotaResponse> buscaPorEmpresaUfCfop(EmpresaEnum empresa, String uf, Integer cfop){

        List<Nota> lst = notaRepository.findByEmpresaAndUfAndCfop(empresa, uf.toUpperCase(), cfop);
        return lst.stream().map(this::getResponse).toList();
    }

    public List<NotaResponse> buscaPorEmpresaUf(EmpresaEnum empresa, String uf){

        List<Nota> lst = notaRepository.findByEmpresaAndUf(empresa, uf.toUpperCase());
        return lst.stream().map(this::getResponse).toList();
    }

    public NotaResponse inserir(NotaRequest request){
        Nota nota = new Nota();
        BeanUtils.copyProperties(request, nota);
        salvar(nota);

        relatorioService.processaPorDataUF(nota.getDataNota(), nota.getUf());
        return getResponse(nota);
    }

    public NotaResponse alterar(String id, NotaRequest request){
        Nota nota = notaRepository.findById(id).orElseThrow(NotFoundException::new);
        BeanUtils.copyProperties(request, nota);
        salvar(nota);

        relatorioService.processaPorDataUF(nota.getDataNota(), nota.getUf());
        return getResponse(nota);
    }

    public void remover(String id){
        Nota nota = notaRepository.findById(id).orElseThrow(NotFoundException::new);
        notaRepository.delete(nota);
        relatorioService.processaPorDataUF(nota.getDataNota(), nota.getUf());
    }

    public List<NotaResponse> buscaPorDataUf(LocalDate data, String uf){

        List<Nota> lst = notaRepository.findByDataNotaAndUf(data, uf.toUpperCase());
        return lst.stream().map(this::getResponse).toList();
    }

    public List<NotaResponse> buscaPorData(LocalDate data){

        List<Nota> lst = notaRepository.findByDataNota(data);
        return lst.stream().map(this::getResponse).toList();
    }

    public void importAll(){
        notaRepository.deleteAll();
        try {
            lerArquivo(new InputStreamReader(new FileInputStream(arquivo), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected Nota quebrar(String linha){

        System.out.println(linha);

        String[] split = linha.split("\t");

        return Nota.builder()
                .dataNota(DateUtils.toLocalDate(split[0]))
                .empresa(EmpresaEnum.valueOf(split[1]))
                .uf(split[2])
                .cfop(Integer.parseInt(split[6]))
                .emitidarecebida(split[3])
                .qtdeNfs(Long.parseLong(split[4].replace(".","")))
                .valorTotal(CurrencyUtils.toDouble(split[5]))
                .build();


    }

    protected void salvar(Nota nota){
        notaRepository.save(nota);
    }

    public NotaResponse getResponse(Nota nota){
        NotaResponse response = new NotaResponse();
        BeanUtils.copyProperties(nota, response);
        return response;
    }
}
