package com.gpa.tributario.gerencial.repository;

import com.gpa.tributario.gerencial.dto.SomaDto;
import com.gpa.tributario.gerencial.entity.Faturamento;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FaturamentoRepository extends MongoRepository<Faturamento, String> {

    List<Faturamento> findByDataAndUf(LocalDate data, String uf);
    List<Faturamento> findByData(LocalDate data);
    List<Faturamento> findByUf(String uf);

    @Aggregation(pipeline = {"{$match: {'data': ?0, 'uf': ?1}}", "{$group: {_id: null, total: {$sum: '$faturamento'}}}"})
    SomaDto sumFaturamentoByDataAndUf(LocalDate data, String uf);

    Long deleteByData(LocalDate data);

    @Aggregation(pipeline = {"{$match: {'data': {'$gte' : ?0}}}", "{$group: {_id: '$data', total: {$sum: '$faturamento'}}}"})
    List<SomaDto> sumFaturamentoByData(LocalDate data);

}
