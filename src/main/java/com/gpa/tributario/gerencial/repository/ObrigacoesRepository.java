package com.gpa.tributario.gerencial.repository;

import com.gpa.tributario.gerencial.dto.CountDto;
import com.gpa.tributario.gerencial.entity.Obrigacoes;
import com.gpa.tributario.gerencial.enuns.ObrigacaoEnum;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ObrigacoesRepository extends MongoRepository<Obrigacoes, String> {

    List<Obrigacoes> findAllByOrderByUF();

    List<Obrigacoes> findByUF(String uf);

    Optional<Obrigacoes> findByLojaAndObrigacao(String loja, ObrigacaoEnum Obrigacao);

    Optional<Obrigacoes> findByIeAndObrigacao(String ie, ObrigacaoEnum Obrigacao);

    @Aggregation(pipeline = {"{$group: {_id: '$UF', total: {$sum: 1}}}","{$project: { _id: 0, chave: '$_id', total: 1}}","{$sort: {chave : 1}}"})
    List<CountDto> findCountGroupByUf();

    @Aggregation(pipeline = {"{$match : {'concluido': true}}","{$group: {_id: '$UF', total: {$sum: 1}}}","{$project: { _id: 0, chave: '$_id', total: 1}}", "{$sort: {chave : 1}}]"})
    List<CountDto> findCountConcluidoGroupByUf();
}
