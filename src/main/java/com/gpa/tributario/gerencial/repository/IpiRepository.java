package com.gpa.tributario.gerencial.repository;

import com.gpa.tributario.gerencial.entity.Ipi;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IpiRepository extends MongoRepository<Ipi, String> {

    List<Ipi> findByDataAndUfAndLoja(LocalDate data, String uf, Integer loja);
    List<Ipi> findByDataAndLoja(LocalDate data, Integer loja);
    List<Ipi> findByUfAndLoja(String uf, Integer loja);
    List<Ipi> findByDataAndUf(LocalDate data, String uf);
    List<Ipi> findByData(LocalDate data);
    Long deleteByData(LocalDate data);
}
