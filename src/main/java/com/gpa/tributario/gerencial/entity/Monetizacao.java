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
@Document(collection = "monetizacao")
public class Monetizacao {

    @Id
    private String id;
    private LocalDate data;
    private String uf;
    private double saldoAnterior;
    private double ajuste;
    private double movimentacao;
    private double monetizacao;
    private double saldoFinal;
    private String filler;
    private double estimado;
    private double realizado;

}
