package com.gpa.tributario.gerencial.repository;

import com.gpa.tributario.gerencial.entity.Relatorio;
import com.gpa.tributario.gerencial.entity.RelatorioID;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RelatorioRepository extends MongoRepository<Relatorio, RelatorioID> {

    @Query("{'id.data': ?0}")
    List<Relatorio> findByData(LocalDate data);
}
