package com.gpa.tributario.gerencial.repository;

import com.gpa.tributario.gerencial.entity.Fechamento;
import com.gpa.tributario.gerencial.enuns.EmpresaEnum;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FechamentoRepository extends MongoRepository<Fechamento, String> {

    List<Fechamento> findByDataFechamentoAndIdLancamentoAndUfOperacao(LocalDate dataFechamento, Integer idLancamento, String ufOperacao);

    List<Fechamento> findByDataFechamentoAndUfOperacao(LocalDate dataFechamento, String ufOperacao);

    List<Fechamento> findByDataFechamentoAndUfOperacaoAndLancamento(LocalDate dataFechamento, String ufOperacao, String lancamento);

    List<Fechamento> findByDataFechamentoAndUfOperacaoAndLancamentoAndTipo(LocalDate dataFechamento, String ufOperacao, String lancamento, String tipo);
    List<Fechamento> findByDataFechamentoAndUfOperacaoAndTipoOrderByTipo(LocalDate dataFechamento, String ufOperacao, String tipo);

    List<Fechamento> findByDataFechamentoAndUfOperacaoOrderByTipo(LocalDate dataFechamento, String ufOperacao);

    List<Fechamento> findByIdLancamentoAndUfOperacao(Integer idLancamento, String ufOperacao);

    List<Fechamento> findByIdLancamento(Integer idLancamento);

    List<Fechamento> findByDataFechamento(LocalDate dataFechamento);

    //@Query(value = "{'dataFechamento': { $gte: ?0, $lte: ?1 }, 'ufOperacao': ?2, 'empresa': ?3, 'tipo': ?4, 'lancamento': ?5 }", sort = "{'dataFechamento': 1}")
    @Aggregation(pipeline = {"{'$match' : {'dataFechamento' : {'$gte' : ?0,'$lte' :  ?1}, " +
            "'ufOperacao': ?2," +
            "'empresa': ?3," +
            "'tipo': ?4," +
            "'lancamento': ?5}}",
            "{'$group' : {'_id' : {'dataFechamento' : '$dataFechamento'}," +
            "'valorTributarioCompliance' : {'$sum' : '$valorTributarioCompliance'}}}",
            "{'$project' : {'dataFechamento' : '$_id.dataFechamento'," +
            "'valorTributarioCompliance' : '$valorTributarioCompliance','_id' : 0}}",
            "{'$sort' : {'dataFechamento' : 1}}"})
    List<Fechamento> findByCompare(LocalDate dataFechamentoInit, LocalDate dataFechamento, String uf, EmpresaEnum empresa, String tipo, String lancamento);

    Long deleteByDataFechamento(LocalDate dataFechamento);

    @Aggregation(pipeline = {"{'$match' : {'dataFechamento' : { '$date' : ?0},'ufOperacao' : ?1}}","{'$group' : {'_id' : {'lancamento' : '$lancamento'}}}","{'$project' : {'lancamento' : '$_id.lancamento','_id' : 0}}", "{'$sort' : {'lancamento' : 1}}"})
    List<Fechamento> findLancamentoGroupBy(LocalDate dataFechamento, String uf);

    @Aggregation(pipeline = {"{'$match' : {'dataFechamento' : { '$date' : ?0},'ufOperacao' : ?1, 'lancamento': ?2}}","{'$group' : {'_id' : {'tipo' : '$tipo'}}}","{'$project' : {'tipo' : '$_id.tipo','_id' : 0}}", "{'$sort' : {'tipo' : 1}}"})
    List<Fechamento> findTipoLancamentoGroupBy(LocalDate dataFechamento, String uf, String lancamento);

    @Aggregation(pipeline = {"{'$match' : {'dataFechamento' : { '$date' : ?0},'ufOperacao' : ?1}}","{'$group' : {'_id' : {'tipo' : '$tipo'}}}","{'$project' : {'tipo' : '$_id.tipo','_id' : 0}}", "{'$sort' : {'tipo' : 1}}"})
    List<Fechamento> findTipoUfGroupBy(LocalDate dataFechamento, String uf);


}
