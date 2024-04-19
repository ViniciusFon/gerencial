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
@Document(collection = "obriAcess")
public class ObriAcess {

    @Id
    private String id;
    private LocalDate dataObri;
    private String empresa;
    private String uf;
    private String codEstabelecimento;
    private String cnpj;
    private String ie;
    private int dapiMg;
    private int giaSt;
    private int devecRj;
    private int sintegra;
    private int giaRs;
    private int dmaBa;
    private int giaSp;
    private int sped;
    private int dimpMktpSp;
    private int dipamSp;
    private int total;
    private String formatoBandeira;
    private String im;
    private String cnpjSit;
    private String ieSit;
    private String imSit;
    private String ac01GPA;
    private boolean emOperacao;
    private String endereco;
    private String bairro;
    private String cep;
    private LocalDate dtAberturaCnpj;
    private String cnaePrincipal;

}
