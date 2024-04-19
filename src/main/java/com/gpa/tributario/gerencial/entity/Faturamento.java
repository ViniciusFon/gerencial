package com.gpa.tributario.gerencial.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "faturamento")
public class Faturamento {

    private String id;
    private LocalDate data;
    private String uf;
    private double faturamento;
    private double icmsVendas;
}
