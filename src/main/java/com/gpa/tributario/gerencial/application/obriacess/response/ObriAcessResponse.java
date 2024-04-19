package com.gpa.tributario.gerencial.application.obriacess.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ObriAcessResponse {

    private String id;
    private LocalDate dataObri;
    private String empresa;
    private String uf;
    private String codEstabelecimento;
    private String cnpj;
    private String ie;
    private Integer dapiMg;
    private Integer giaSt;
    private Integer devecRj;
    private Integer sintegra;
    private Integer giaRs;
    private Integer dmaBa;
    private Integer giaSp;
    private Integer sped;
    private Integer dimpMktpSp;
    private Integer dipamSp;
    private Integer total;
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
