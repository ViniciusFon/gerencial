package com.gpa.tributario.gerencial.entity;

import com.gpa.tributario.gerencial.enuns.EmpresaEnum;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "nota")
public class Nota {

    @Id
    private String id;
    private LocalDate dataNota;
    private EmpresaEnum empresa;
    private String uf;
    private int cfop;
    private String emitidarecebida;
    private long qtdeNfs;
    private double valorTotal;
    private long ctes;
}
