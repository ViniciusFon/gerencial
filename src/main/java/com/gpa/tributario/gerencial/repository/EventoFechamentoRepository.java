package com.gpa.tributario.gerencial.repository;

import com.gpa.tributario.gerencial.dto.CountDto;
import com.gpa.tributario.gerencial.entity.EventoFechamento;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventoFechamentoRepository extends MongoRepository<EventoFechamento, String> {

    List<EventoFechamento> findByUF(String uf);

    @Aggregation(pipeline = {"{$group: {_id: '$UF', total: {$sum: 1}}}","{$project: { _id: 0, chave: '$_id', total: 1}}","{$sort: {chave : 1}}"})
    List<CountDto> findCountGroupByUf();

    @Aggregation(pipeline = {"{$match : {'concluido': true}}","{$group: {_id: '$UF', total: {$sum: 1}}}","{$project: { _id: 0, chave: '$_id', total: 1}}", "{$sort: {chave : 1}}]"})
    List<CountDto> findCountConcluidoGroupByUf();
}
