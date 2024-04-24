package com.gpa.tributario.gerencial.application.obriacess;

import com.gpa.tributario.gerencial.application.ArquivoBaseService;
import com.gpa.tributario.gerencial.application.obriacess.request.ObriAcessRequest;
import com.gpa.tributario.gerencial.application.obriacess.response.ObriAcessResponse;
import com.gpa.tributario.gerencial.application.relatorio.RelatorioService;
import com.gpa.tributario.gerencial.entity.ObriAcess;
import com.gpa.tributario.gerencial.infrastructure.exception.NotFoundException;
import com.gpa.tributario.gerencial.repository.ObriAcessRepository;
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

import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
public class ObriAcessService extends ArquivoBaseService<ObriAcess> {

    @Value("${arquivo.obriAcess.path}")
    private String arquivo;

    @Autowired
    private RelatorioService relatorioService;

    @Autowired
    private ObriAcessRepository obriAcessRepository;

    public ObriAcessResponse buscaPorID(String id){

        ObriAcess obriAcess = obriAcessRepository.findById(id).orElseThrow(NotFoundException::new);
        return getResponse(obriAcess);
    }

    public List<ObriAcessResponse> buscaPorDataUfCodEstabelecimento(LocalDate data, String uf, String codEstabelecimento ){

        List<ObriAcess> lst = obriAcessRepository.findByDataObriAndUfAndCodEstabelecimento(data, uf.toUpperCase(), codEstabelecimento);

        return lst.stream().map(this::getResponse).toList();
    }

    public List<ObriAcessResponse> buscaPorDataUf(LocalDate data, String uf ){

        List<ObriAcess> lst = obriAcessRepository.findByDataObriAndUfOrderByCodEstabelecimento(data, uf.toUpperCase());

        return lst.stream().map(this::getResponse).toList();
    }

    public List<ObriAcessResponse> buscaPorDataCnpj(LocalDate data, String cnpj){

        List<ObriAcess> lst = obriAcessRepository.findByDataObriAndCnpj(data, mascaraCnpj(cnpj));

        return lst.stream().map(this::getResponse).toList();
    }

    public List<ObriAcessResponse> buscaPorCnpj(String cnpj ){

        List<ObriAcess> lst = obriAcessRepository.findByCnpj(mascaraCnpj(cnpj));

        return lst.stream().map(this::getResponse).toList();
    }

    public ObriAcessResponse inserir(ObriAcessRequest request){

        ObriAcess obriAcess = new ObriAcess();
        BeanUtils.copyProperties(request, obriAcess);
        obriAcess.setCnpj(mascaraCnpj(request.getCnpj()));
        salvar(obriAcess);

        relatorioService.processaPorDataUF(obriAcess.getDataObri(), obriAcess.getUf());
        return getResponse(obriAcess);
    }

    public ObriAcessResponse alterar(String id, ObriAcessRequest request){

        ObriAcess obriAcess = obriAcessRepository.findById(id).orElseThrow(NotFoundException::new);
        BeanUtils.copyProperties(request, obriAcess);
        obriAcess.setCnpj(mascaraCnpj(obriAcess.getCnpj()));
        salvar(obriAcess);

        relatorioService.processaPorDataUF(obriAcess.getDataObri(), obriAcess.getUf());
        return getResponse(obriAcess);
    }

    public void remover(String id){

        ObriAcess obriAcess = obriAcessRepository.findById(id).orElseThrow(NotFoundException::new);
        obriAcessRepository.delete(obriAcess);
        relatorioService.processaPorDataUF(obriAcess.getDataObri(), obriAcess.getUf());
    }

    public void importAll(){
        obriAcessRepository.deleteAll();
        try {
            lerArquivo(new InputStreamReader(new FileInputStream(arquivo), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected ObriAcess quebrar(String linha){

        String[] split = linha.split("\t");

        return ObriAcess.builder()
                .dataObri(DateUtils.toLocalDate(split[0]))
                .empresa(split[1])
                .uf(split[2])
                .codEstabelecimento(split[3])
                .cnpj(mascaraCnpj(split[4]))
                .ie(split[5])
                .dapiMg(convertInteger(split[6]))
                .giaSt(convertInteger(split[7]))
                .devecRj(convertInteger(split[8]))
                .sintegra(convertInteger(split[9]))
                .giaRs(convertInteger(split[10]))
                .dmaBa(convertInteger(split[11]))
                .giaSp(convertInteger(split[12]))
                .sped(convertInteger(split[13]))
                .dimpMktpSp(convertInteger(split[14]))
                .dipamSp(convertInteger(split[15]))
                .total(convertInteger(split[16]))
                .formatoBandeira(split[17])
                .im(split[18])
                .cnpjSit(split[19])
                .imSit(split[20])
                .ieSit(split[21])
                .ac01GPA(split[22])
                .emOperacao("SIM".equals(split[23]))
                .endereco(split[24])
                .bairro(split[25])
                .cep(split[26])
                .dtAberturaCnpj(DateUtils.toLocalDate(split[27]))
                .cnaePrincipal(split[28]).build();

    }

    protected void salvar(ObriAcess obriAcess){
        obriAcessRepository.save(obriAcess);
    }

    private String mascaraCnpj(String cnpj){
        return new StringBuilder(cnpj.replaceAll("\\D",""))
        .insert(2, ".")
        .insert(6, ".")
        .insert(10, "/")
        .insert(15, "-").toString();
    }
    
    private int convertInteger(String value){
        if(isBlank(value)) return 0;
        
        return Integer.parseInt(value);
    }

    private ObriAcessResponse getResponse(ObriAcess obriAcess){
        ObriAcessResponse response = new ObriAcessResponse();
        BeanUtils.copyProperties(obriAcess, response);
        return response;
    }

}
