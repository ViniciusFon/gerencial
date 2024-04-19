package com.gpa.tributario.gerencial.repository;

import com.gpa.tributario.gerencial.entity.Apuracao;
import com.gpa.tributario.gerencial.enuns.EmpresaEnum;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ApuracaoRepository extends MongoRepository<Apuracao, String> {

    List<Apuracao> findByDataAndEmpresaAndUf(LocalDate data, EmpresaEnum empresa, String uf);
    List<Apuracao> findByDataAndEmpresa(LocalDate data, EmpresaEnum empresa);
    List<Apuracao> findByEmpresaAndUf(EmpresaEnum empresa, String uf);
    List<Apuracao> findByDataAndUf(LocalDate data, String uf);
    List<Apuracao> findByData(LocalDate data);
    Long deleteByData(LocalDate data);
}
