package com.gpa.tributario.gerencial.repository;

import com.gpa.tributario.gerencial.entity.Icms;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IcmsRepository extends MongoRepository<Icms, String> {

    List<Icms> findByDataIcmsAndCodEstadoAndCfop(LocalDate dataIcms, String codEstado, Integer cfop);
    List<Icms> findByDataIcmsAndCodEstado(LocalDate dataIcms, String codEstado);
    List<Icms> findByCodEstadoAndCfop(String codEstado, Integer cfop);
    List<Icms> findByDataIcmsAndCodEstadoAndDescricao(LocalDate dataIcms, String codEstado, String descricao);
    List<Icms> findByDataIcms(LocalDate dataIcms);
    Long deleteByDataIcms(LocalDate dataIcms);
}
