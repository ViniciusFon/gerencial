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
@Document(collection = "icms")
public class Icms {

    @Id
    private String id;
    private LocalDate dataIcms;
    private String codEstado;
    private int cfop;
    private String descricao;
    private double valorContab;
    private double baseTribu;
    private double valorIcms;
    private double baseIsenta;
    private double baseOutras;
}
