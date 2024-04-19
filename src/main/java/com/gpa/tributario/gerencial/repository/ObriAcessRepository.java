package com.gpa.tributario.gerencial.repository;

import com.gpa.tributario.gerencial.entity.ObriAcess;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ObriAcessRepository extends MongoRepository<ObriAcess, String> {

    List<ObriAcess> findByDataObriAndUfAndCodEstabelecimento(LocalDate data, String uf, String codEstabelecimento);
    List<ObriAcess> findByDataObriAndUf(LocalDate data, String uf);
    List<ObriAcess> findByCnpj(String cnpj);
    List<ObriAcess> findByDataObriAndCnpj(LocalDate data, String cnpj);
    Long deleteByDataObri(LocalDate dataObri);

}
