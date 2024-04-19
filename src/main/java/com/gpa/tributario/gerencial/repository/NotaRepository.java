package com.gpa.tributario.gerencial.repository;

import com.gpa.tributario.gerencial.entity.Nota;
import com.gpa.tributario.gerencial.enuns.EmpresaEnum;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NotaRepository extends MongoRepository<Nota, String> {

    List<Nota> findByDataNotaAndEmpresaAndUfAndCfop(LocalDate dataNota, EmpresaEnum empresa, String uf, Integer cfop);
    List<Nota> findByDataNotaAndEmpresaAndUf(LocalDate dataNota, EmpresaEnum empresa, String uf);
    List<Nota> findByEmpresaAndUfAndCfop(EmpresaEnum empresa, String uf, Integer cfop);
    List<Nota> findByEmpresaAndUf(EmpresaEnum empresa, String uf);
    List<Nota> findByDataNotaAndUf(LocalDate dataNota, String uf);
    List<Nota> findByDataNota(LocalDate dataNota);
    Long deleteByDataNota(LocalDate dataNota);

}
