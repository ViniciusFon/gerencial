package com.gpa.tributario.gerencial.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ipi")
public class Ipi {

    @Id
    private String id;
    private LocalDate data;
    private String uf;
    private int loja;
    private double saldoCreditAnterior;
    private double debitoSaida;
    private double debitoOutro;
    private double debitoTdebito;
    private double creditoEntrada;
    private double creditoOutro;
    private double creditoTcredito;
    private double deducaoRedutora;
    private double deducaoCredora;
    private double saldoCredor;
    private double saldoDevedor;
}
