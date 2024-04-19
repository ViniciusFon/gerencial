package com.gpa.tributario.gerencial.repository;

import com.gpa.tributario.gerencial.entity.Monetizacao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MonetizacaoRepository extends MongoRepository<Monetizacao, String> {

    List<Monetizacao> findByDataAndUf(LocalDate data, String uf );

    List<Monetizacao> findByData(LocalDate data );
    Long deleteByData(LocalDate data);

}
