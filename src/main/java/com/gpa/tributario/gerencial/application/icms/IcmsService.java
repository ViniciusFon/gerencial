package com.gpa.tributario.gerencial.application.icms;

import com.gpa.tributario.gerencial.application.ArquivoBaseService;
import com.gpa.tributario.gerencial.application.faturamento.FaturamentoService;
import com.gpa.tributario.gerencial.application.faturamento.request.FaturamentoRequest;
import com.gpa.tributario.gerencial.application.faturamento.response.FaturamentoResponse;
import com.gpa.tributario.gerencial.application.icms.request.IcmsRequest;
import com.gpa.tributario.gerencial.application.icms.response.IcmsResponse;
import com.gpa.tributario.gerencial.entity.Icms;
import com.gpa.tributario.gerencial.infrastructure.exception.NotFoundException;
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
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

@Service
public class IcmsService extends ArquivoBaseService<Icms> {

    @Value("${arquivo.icms.path}")
    private String arquivo;

    @Autowired
    private FaturamentoService faturamentoService;

    @Autowired
    private IcmsRepository icmsRepository;


    public IcmsResponse buscaPorID(String id){

        Icms icms = icmsRepository.findById(id).orElseThrow(NotFoundException::new);

        return getResponse(icms);
    }

    public List<IcmsResponse> buscarPorDataEstadoCfop(LocalDate data, String codEstado, Integer cfop){

        List<Icms> lst = icmsRepository.findByDataIcmsAndCodEstadoAndCfop(data, codEstado.toUpperCase(), cfop);

        return lst.stream().map(this::getResponse).toList();
    }

    public List<IcmsResponse> buscarPorDataEstado(LocalDate data, String codEstado){

        List<Icms> lst = icmsRepository.findByDataIcmsAndCodEstado(data, codEstado.toUpperCase());

        return lst.stream().map(this::getResponse).toList();
    }

    public List<IcmsResponse> buscarPorEstadoCfop(String codEstado, Integer cfop){

        List<Icms> lst = icmsRepository.findByCodEstadoAndCfop(codEstado.toUpperCase(), cfop);

        return lst.stream().map(this::getResponse).toList();
    }

    public List<IcmsResponse> buscarPorData(LocalDate data){

        List<Icms> lst = icmsRepository.findByDataIcms(data);

        return lst.stream().map(this::getResponse).toList();
    }

    public IcmsResponse inserir(IcmsRequest request){

        Icms icms = new Icms();
        BeanUtils.copyProperties(request, icms);

        salvar(icms);
        //Altera o valor de IcmsVenda do faturamento, pois o ICMS aumentou
        List<FaturamentoResponse> lstFat = faturamentoService.buscaPorDataUf(request.getDataIcms(), request.getCodEstado());
        lstFat.forEach(this::alteraIcmsDoFaturamento);

        return getResponse(icms);
    }

    public IcmsResponse alterar(String id, IcmsRequest request){

        Icms icms = icmsRepository.findById(id).orElseThrow(NotFoundException::new);
        BeanUtils.copyProperties(request, icms);

        salvar(icms);

        //Altera o valor de IcmsVenda do faturamento, pois o ICMS mudou
        List<FaturamentoResponse> lstFat = faturamentoService.buscaPorDataUf(request.getDataIcms(), request.getCodEstado());
        lstFat.forEach(this::alteraIcmsDoFaturamento);

        return getResponse(icms);
    }

    public void remover(String id){

        Icms icms = icmsRepository.findById(id).orElseThrow(NotFoundException::new);

        icmsRepository.delete(icms);

        //Altera o valor de IcmsVenda do faturamento, pois o ICMS mudou
        List<FaturamentoResponse> lstFat = faturamentoService.buscaPorDataUf(icms.getDataIcms(), icms.getCodEstado());
        lstFat.forEach(this::alteraIcmsDoFaturamento);
    }

    public void importAll(){
        icmsRepository.deleteAll();
        try {
            lerArquivo(new InputStreamReader(new FileInputStream(arquivo), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected Icms quebrar(String linha){

        System.out.println(linha);

        String[] split = linha.split("\t");

        return Icms.builder()
                .dataIcms(DateUtils.toLocalDate(split[0]))
                .codEstado(split[1])
                .cfop(Integer.parseInt(split[2]))
                .descricao(split[3])
                .valorContab(CurrencyUtils.toDouble(split[4]))
                .baseTribu(CurrencyUtils.toDouble(split[5]))
                .valorIcms(CurrencyUtils.toDouble(split[6]))
                .baseIsenta(CurrencyUtils.toDouble(split[7]))
                .baseOutras(CurrencyUtils.toDouble(split[8])).build();

    }

    protected void salvar(Icms icms){
        icmsRepository.save(icms);
    }

    private IcmsResponse getResponse(Icms icms){
        IcmsResponse response = new IcmsResponse();
        BeanUtils.copyProperties(icms, response);
        return response;
    }

    private void alteraIcmsDoFaturamento(FaturamentoResponse response){
        faturamentoService.alterar(response.getId(), new FaturamentoRequest(response.getData(), response.getUf(), response.getFaturamento()));
    }

}
